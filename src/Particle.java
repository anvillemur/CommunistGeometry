public class Particle{
    private double[][]s;
    private int dimensions;
    private int[] degrees;
    private static double particleid;
    public static double particlecount;
    public Particle(int d, int[] de, int m, double[][]displacement){
        for(int i=0; i<d; i++){
            if (displacement[i].length!=de[i]+1) {
                System.out.println("You do not print error message. Error message prints you (Degree in dimension " + i + " does not match.)");
                return;
            }
        }
        degrees=de;
        dimensions=d;
        s=displacement;
        particleid=particlecount;
        particlecount+=1;
    }

    //s[d][i] is the coefficient of x^[n-i] for the polynomial for the dimension of d.
    //Basically, if s[0]=[1,2,3,4], then the position of x(t)=x^3+2x^2+3x+4
    //degrees[i] stores the degree of the polynomial for dimension i
    //this is capable of producing particles of an arbitrary number of dimensions
    //I have no idea what I am going to use particleid for
    //position(time) returns the position of the particle as a position vector
    //upcoming features: velocity, acceleration, force, momentum, energy, gravity.
    public double[] position(int time){
        double[] ans =new double[dimensions];
        for(int i=0; i<dimensions; i++){
            ans[i]=0;
        }
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<degrees[i]+1; j++){
                ans[i]+=s[i][j]*Math.pow(time, degrees[i]-j);
            }
        }
        return ans;
    }


}
