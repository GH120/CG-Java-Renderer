package modelos.raytracers;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import algebra.*;
import modelos.*;
import modelos.fontes.*;
import modelos.reflexoes.Superficie;

//Com iluminação global => DEPRECATED
//Refazer quase tudo, na verdade, basta fazer o sampling das fontes extensas
//Basicamente, atirar vários raios e ver quais acertam a fonte extensa, transformar esses pontos em fontes pontuais direcionais e adicionar a cena
public class GIluminationTracer extends Raytracer{

    int depth = 1;

    //CalcularSamplesDaLuz => pega as luzes da cena e calcula samples delas
    //Com esses samples, vamos ter pontos onde temos certeza que a luz incide
    //Usamos esses pontos como possíveis direções de reflexão para o raytracing
    //Usar uma estrutura de dados de otimização? => octree
    //Depois adicionar peso para os samples mais pesados

    //Além disso, sobrescrever o iluminar, fontes não pontuais extensas
    //Quando o raio do pixel bater no objeto, calcular os samples da luz com um subalgoritmo
    //Esses samples vão ser raios calculados com reflexão e tudo
    //Mas eles só veem se colidiu com alguma das luzes extensas
    //Soma as contribuições desses raios multiplicando pelo coeficiente?
    //Raios com sombra contam para serem divididos, 
    //se 30 samples acertaram 15 em fontes de luzes dos quais 10 tem sombra, então multiplicar por 33% a intensidade da luz

    public void render() {

        gerarRaios();
    
        calcularRaios();
      }
    
    public void calcularRaios(){
  
      while(!raios.isEmpty()){
        
        Raio raio = raios.removeFirst();
  
        int l = raio.linha; int c = raio.coluna;
        
        buffer[c][l] = buffer[c][l].mais(buscarCor(raio, raios));
      }
    }

    public Vetor buscarCor(Raio raio, List<Raio> pilha){

        Ponto ponto = cena.objetos.colisao(raio.origem, raio.direcao);

        //Se houver um ponto de colisão com uma fonte extensa, retorna sua contribuição
        for(Extensa fonte : cena.fontesExtensas){
          Ponto colisaoFonteExtensa = fonte.colisao(raio);

          if(raio.pontoMaisProximo(colisaoFonteExtensa, ponto)){
            return fonte.If.mult(raio.intensidade);
          }
        }

        if (ponto == null)
            return cena.background.mult(raio.intensidade);

        if( raio.interno) 
            ponto.normal = ponto.normal.vezes(-1);
    
        if(raio.profundidade < depth){

          Superficie superficie = ponto.objeto.superficie;
    
          if(superficie != null) superficie.refletir(ponto, raio, pilha);
    
        }
        
        Vetor luz = iluminar(ponto, raio.direcao);
    
        luz  = luz.mult(raio.intensidade);
    
        return luz;
    }

    public void gerarRaios() {

        raios = new LinkedList<>();
    
        double w = camera.wJanela;
        double h = camera.hJanela;
    
        double deltax = w / linhas;
        double deltay = h / colunas;
    
        for (int l = 0; l < linhas; l++) {
          double y = h / 2 - deltay / 2 - deltay * l;
          for (int c = 0; c < colunas; c++) {
            double x = w / 2 - deltax / 2 - deltax * c;
            
            gerarRaio(x, y, c, l);
          }
        }
      }
    
      void gerarRaio(double x, double y, int c, int l) {
    
        Raio raio = new Raio();
    
        raio.linha = l;
    
        raio.coluna = c;
    
        // posicao x,y da camera transformada para as coordenadas de mundo
        raio.origem = camera.toWorld((new Posicao(x, y, 0)));
    
        // raio que incide sobre o pixel[c][l]
        raio.direcao = camera.projecao.getDirecao(raio.origem);
    
        // O coeficiente de reflexão de multiplas colisões
        raio.intensidade = new Vetor(1, 1, 1);
    
        raio.origem = raio.origem.tresD();
        raio.direcao = raio.direcao.tresD();
    
        raios.add(raio);
    
      }

    // Vetor iluminarExtenso(Ponto ponto, Raio raio){

    //     Vetor iluminacao = new Vetor(0,0,0);

    //     //Leva agora em conta as luzes extensas

    //     for(int i=0; i < numberSamples; i++)
    //         refletir(ponto, raio, samples);

    //     for(int colisions = 0; colisions < numberSamples; colisions++){ 
            
    //        raio = samples.pop();

    //        for(Extensa fonte : cena.fontesExtensas){

    //             Ponto colisao = fonte.colisao(raio);

    //             if(colisao != null) {
    //                 iluminacao.add(fonte.luz(colisao,raio));
    //                 // colisao.printar();
    //             }

    //        }
    //     }

    //     iluminacao.vezes(1/colisionCount);

    //     return iluminacao;
    // }

    void refletir(Ponto ponto, Raio raio, LinkedList<Raio> pilha){
        

        Superficie superficie = ponto.objeto.superficie;

        if(superficie == null) return;

        superficie.refletir(ponto, raio, pilha);
    }

    private void photonMapping(){

      Random rand = new Random();

      ArrayList<Fonte> photons = new ArrayList<>();

      double atenuacao = 0.001;

      //APENAS FUNCIONA PARA VALORES DE Y NEGATIVOS, GENERALIZAR DEPOIS
      for(int i=0; i < 1000; i++)
      for(Fonte fonte : cena.fontes){

        Vetor direcao = new Vetor(1 - 2*rand.nextDouble(), -rand.nextDouble(), 1 - 2*rand.nextDouble());

        Ponto photonColision = cena.objetos.colisao(fonte.posicao, direcao.unitario());

        if(photonColision == null) continue;

        Fonte stickyPhoton = new Spot(photonColision.pos.mais(direcao.vezes(-5)).valores)
                                .setAngulo(90)
                                .setDirecao(photonColision.normal.valores);

        stickyPhoton.If = photonColision.getKd().mult(fonte.If).vezes(atenuacao);

        stickyPhoton.printar();

        photons.add(stickyPhoton);
      }
      
      for(Fonte photon : photons){
        cena.fontes.add(photon);
        photon.posicao.printar();
        photon.If.printar();
      }
    }

    public Raytracer setCena(Cena cena){

      super.setCena(cena);

      for(Extensa fonte : cena.fontesExtensas)
          for(int i=0; i < fonte.samples; i++)
            cena.fontes.add(fonte.sample());

      photonMapping();

      return this;
    }
}
