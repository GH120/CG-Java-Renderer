package modelos.cenas;
import algebra.*;
import modelos.*;
import modelos.fontes.*;
import modelos.objetos.*;
import modelos.reflexoes.*;

public class Teste4 extends Cena{

  public Teste4(){
    
    Objetos(

            new MalhaOBJ("car.obj")
            .construir()
            .BoundingVolume()
            .setKe(0.752,0.0549,0.10196)
            .setKa(0.752,0.0549,0.10196)
            .setKd(0.752,0.0549,0.10196)
            .aplicar(
                new RotacaoY(-10),
                new Escala(100,100,100),
                new Translacao(-20,-50,-500)
            )
            ,

            // new Plano(2000,-1500,-4000)
            // .setNormal(0,0,1)
            // .setKd(0, 0, 1)
            // .setKa(0, 0, 1)
            // .setKe(0, 0, 1),

            // new Plano(2000,-1500,0)
            // .setNormal(-1,0,0)
            // .setKd(0, 1, 0)
            // .setKa(0, 1, 0)
            // .setKe(0, 1, 0),

            // new Plano(-2000,-1500,0)
            // .setNormal(1,0,0)
            // .setKd(1, 0, 0)
            // .setKa(1, 0, 0)
            // .setKe(1, 0, 0),

            new Piso(
                    (Plano) new Plano(0,-1500,0)
                           .setNormal(0,1,0)
                           .setKd(1,1,1)
                           .setKa(1,1,1)
                           .setKe(1.0, 1.0, 1.0),
                    "images.jpeg")
            .setEscala(10)
    );

    background = new Vetor(0.1,0.1,0.1);

    Fontes(
      new Fonte(-100, 700, 700).setCor(0.5f,0.5f,0.5f)
    );

    FontesExtensas(

    new Extensa(
      new Vetor(0,100,0),
      new Vetor(100,100,0),
      new Vetor(100, 100, -100),
      new Vetor(0,100, -100)
)

    );
  }
}
