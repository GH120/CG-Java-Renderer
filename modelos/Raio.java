package modelos;
import algebra.*;

public class Raio{

  public int      profundidade;
  public int      linha;
  public int      coluna;
  public boolean  interno;

  public Vetor intensidade;
  public Vetor origem;
  public Vetor direcao;

  public Raio(){
    profundidade = 0;
    interno = false;
  }

  Raio(int iteracao){
    profundidade = iteracao;
    interno = false;
  }

  public Raio reflexao(Ponto ponto){

    Raio refletido = new Raio(profundidade+1);

    refletido.linha  = linha;
    refletido.coluna = coluna;
    

    //Direção muda para o raio refletido no ponto
    refletido.direcao =  direcao
                              .menos(
                                  ponto.normal
                                  .vezes(
                                    2*direcao
                                    .escalar(ponto.normal)
                                  )
                              )
                              .unitario();

    //O raio refletido tem origem no ponto de colisão
    refletido.origem = ponto.pos.mais(refletido.direcao);

    //Intensidade diminui pelo fator de reflexão especular
    refletido.intensidade = intensidade.mult(ponto.getKe());

    return refletido;
  }

  public boolean pontoMaisProximo(Ponto ponto1, Ponto ponto2){

    if(ponto1 == null) return false;
    if(ponto2 == null) return true;

    double distancia1 = ponto1.pos.menos(origem).escalar(direcao);
    double distancia2 = ponto2.pos.menos(origem).escalar(direcao);

    return distancia1 < distancia2;
  }

  public Raio refletido(){

    Raio refletido = new Raio(profundidade+1);

    refletido.linha   = linha;
    refletido.coluna  = coluna;
    refletido.interno = interno;

    return refletido;
  }

  public void printar(){
    System.out.println("Intensidade:");
    intensidade.printar();
    System.out.println("Origem:");
    origem.printar();
    System.out.println("Direção:");
    direcao.printar();
    System.out.println("Linha: " + linha + "Coluna: " + coluna);
    System.out.println("è interno "+ interno);
  }
}