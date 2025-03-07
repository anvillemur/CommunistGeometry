import java.util.Objects;

import static java.lang.Math.abs;

public class Vector {
    Rect recform = new Rect(0,0);
    Polar polform = new Polar(0,0);

    public Vector(Rect x) {
        this.recform = x;
        this.polform = Polar.pol(x);
    }

    public Vector(Polar x){
        this.polform = x;
        this.recform = Rect.rec(x);
    }

    public Vector(double a, double b, double c, double d){
        this.recform = new Rect(a,b);
        this.polform = new Polar(c,d);
    }

    public Vector() {

    }

    public static Vector sum(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.setX(lh.recform.getX() + rh.recform.getX());
        cur.setY(lh.recform.getY() + rh.recform.getY());
        return new Vector(cur);
    }

    public static Vector minus(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.setX(lh.recform.getX() - rh.recform.getX());
        cur.setY(lh.recform.getY() - rh.recform.getY());
        return new Vector(cur);
    }

    public static Vector difference(Vector lh, Vector rh){
        Rect cur = new Rect(0,0);
        cur.setX(abs(lh.recform.getX() - rh.recform.getX()));
        cur.setY(abs(lh.recform.getY() - rh.recform.getY()));
        return new Vector(cur);
    }

    public void add(Vector other){
        this.recform.setX(this.recform.getX() + other.recform.getX());
        this.recform.setY(this.recform.getY() + other.recform.getY());
        this.polform = Polar.pol(this.recform);
    }

    public void subtract(Vector other){
        this.recform.setX(this.recform.getX() - other.recform.getX());
        this.recform.setY(this.recform.getY() - other.recform.getY());
        this.polform = Polar.pol(this.recform);
    }

    public static void printRec(Vector x){
        System.out.printf("X: %s, Y: %s\n",Settings.df.format(x.recform.getX()),Settings.df.format(x.recform.getY()));
    }

    public static void printPol(Vector x){
        if (Objects.equals(Settings.OutputAngleFormat, Angle.RADIANS)) {
            System.out.printf("Magnitude: %s, Direction: %s radians\n",Settings.df.format(x.polform.getMag()),Settings.df.format(x.polform.getTheta().getRad()));
        }
        else {
            System.out.printf("Magnitude: %s, Direction: %s degrees\n",Settings.df.format(x.polform.getMag()),Settings.df.format(x.polform.getTheta().getDeg()));
        }
    }


    public static void print(Vector x){
        printRec(x);
        printPol(x);
    }

    public void scalarMult(double x){
        this.polform.scalarMult(x);
        this.recform.scalarMult(x);
    }

    public Vector scale(double x){
        Vector cur = new Vector(0,0,0,0);
        cur.polform = this.polform;
        cur.recform = this.recform;
        cur.polform.scalarMult(x);
        cur.recform.scalarMult(x);
        return cur;
    }
}
