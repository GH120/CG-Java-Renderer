package modelos.objetos;
import algebra.*;
import modelos.*;

public class Triangulo extends Plano{

  public Vetor P2;

public Vetor P3;

  Vetor r1, r2;

  public Triangulo(double ...eixos){
    P1 = new Vetor(eixos); // = centro
  }

  public Triangulo setP2(double ...eixos){
    P2 = new Vetor(eixos);
    return this;
  }
  //r1 e r2 formam um plano 2D
  public Triangulo setP3(double ...eixos){
    P3 = new Vetor(eixos);

    r1 = P2.menos(P1);
    r2 = P3.menos(P1);

    setNormal(eixos);

    return this;
  }

  public Plano setNormal(double ...eixos){
    n = r1.vetorial(r2).unitario();
    return this;
  }

  boolean pertence(Vetor pi){
    
    double razao = r1.vetorial(r2).escalar(n);

    double c1 = P3.menos(pi).vetorial(P1.menos(pi)).escalar(n)/razao;

    double c2 = P1.menos(pi).vetorial(P2.menos(pi)).escalar(n)/razao; 

    double c3  = P2.menos(pi).vetorial(P3.menos(pi)).escalar(n)/razao;
    
    if(c1 < 0 || c2 < 0 || c3 < 0) return false;
    if(c1 > 1 || c2 > 1 || c3 > 1) return false;

    return (c1+c2 <= 1);
  }

  Ponto colisaoObjeto(Vetor p0, Vetor dr){
    
    Ponto ponto = super.colisaoObjeto(p0,dr);
    
    if(ponto == null) return null;

    return (pertence(ponto.pos))? ponto : null;
  }


  public Esfera BoundingBox(){

    Vetor centro = P1.mais(P2).mais(P3).vezes(1/3);

    double d1 = P1.menos(centro).modulo();
    double d2 = P2.menos(centro).modulo();
    double d3 = P3.menos(centro).modulo();

    double raio = (d1 > d2)? d1 : (d2 > d3)? d2 : d3;

    return new Esfera(centro.valores).setRaio(raio);
  }
}