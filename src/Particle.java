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

    //s[i] is the coefficient of x^[n-i]
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
