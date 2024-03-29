package modelos.objetos;
import algebra.*;
import modelos.*;

public class Cone extends Composto{

  Vetor   base, dir, vertice;
  double  altura, raio;
  Circulo inferior;

  public Cone(double ...eixos){
    this.base = new Vetor(eixos);
  }

  //Seta as propriedades dos componentes do objeto composto
  //Ele é um objeto composto do círculo inferior e contorno do cone
  public Cone construir(){
    inferior = new Circulo(base.valores);
    inferior.setNormal(dir.vezes(-1).valores);
    inferior.setRaio(raio);
    componentes.add(inferior);
    componentes.add(getContorno());
    return this;
  }


  //Contorno do cilindro, um objeto simples que exclui o círculo inferior
  Objeto getContorno(){
    
    return new ObjetoSimples() {
      
        Ponto colisaoObjeto(Vetor p0, Vetor dr){
        
          Vetor v = vertice.menos(p0);
      
          double cos2 = altura*altura/(raio*raio + altura*altura);
          
          double a = dr.escalar(dir)*dr.escalar(dir) - dr.escalar(dr)*cos2;
          double b = 2*(v.escalar(dr)*cos2 - v.escalar(dir)*dr.escalar(dir));
          double c = v.escalar(dir)*v.escalar(dir) - v.escalar(v)*cos2;
      
          Vetor ponto = intersecaoRaio(a,b,c,p0,dr);
      
          if(ponto == null) return null;
      
          double height = vertice.menos(ponto).escalar(dir);
          
          if(height > altura || height < 0) return null;
      
          return getPonto(ponto, normal(ponto));
          
        }

        public Esfera BoundingBox(){
          //Raio da esfera que contém o cone

          double raioEsfera = (altura*altura+raio*raio)/(2*altura);

          Vetor R = dir.vezes(raio);

          Vetor centroEsfera = vertice.menos(R);

          return new Esfera(centroEsfera.valores).setRaio(raioEsfera);
        }
    };
  }

  public Cone setTopo(double ...eixos){
    vertice = new Vetor(eixos);
    this.dir = vertice.menos(base).unitario();
    this.altura = vertice.menos(base).modulo();
    return this;
  }

  public Cone setRaio(double raio){
    this.raio = raio;
    return this;
  }

  public Cone setAltura(double altura){
    this.altura = altura;
    return this;
  }

  public Cone setDir(double ...eixos){
    return setTopo(base.mais(new Vetor(eixos).vezes(altura)).valores);
  }

  Vetor normal(Vetor pi){

    Vetor diferenca = vertice.menos(pi);
    Vetor N = diferenca.vetorial(dir);
    Vetor n = diferenca.vetorial(N).unitario();

    return n.vezes(-1);
  }
}