import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Interactor {
    private final HashMap<String,Vector> dataTable = new HashMap<>();
    private final Scanner Reader = new Scanner(System.in);
    private final ArrayList<String> queries = new ArrayList<>();
    public Boolean terminated = (Boolean) false;
    private final HashMap<String,String> aliases = new HashMap<>();

    public void query(){
        Voicelines.askQuery();
    }

    public void acceptQuery(){
        Scanner Parser = new Scanner(Reader.nextLine());
        queries.clear();

        while (Parser.hasNext()){
            queries.add(Parser.next());
        }

        String queryType = queries.removeFirst();
        while (aliases.containsKey(queryType)) { //take care of alias of alias
            queryType = aliases.get(queryType);
        }

        switch(queryType){
            case "terminate" -> terminate();
            case "list" -> list();
            case "new" -> store();
            case "retrieve" -> retrieve();
            case "thanks" -> thank();
            case "set" -> set();
            case "debug" -> debug();
            case "calculate" -> calc();
            case "desmos" -> desmos();
            case "alias" -> alias();
            case "unalias" -> unalias();
            default -> unknownQuery();
        }
    }

    private void terminate(){
        Voicelines.announceTermination();
        terminated = true;
    }

    private void store(){
        String storingType = queries.removeFirst();
        String storingName = queries.removeFirst();
        switch (storingType) {
            case "polar" -> {
                Vector cur = new Vector(0, 0, 0, 0);
                cur.polform.setMag(Double.parseDouble(queries.removeFirst()));
                cur.polform.setThetaInput(Double.parseDouble(queries.removeFirst()));
                cur.recform = Rect.rec(cur.polform);
                dataTable.put(storingName, cur);
            }
            case "rect" -> {
                Vector cur = new Vector(0, 0, 0, 0);
                cur.recform.setX(Double.parseDouble(queries.removeFirst()));
                cur.recform.setY(Double.parseDouble(queries.removeFirst()));
                cur.polform = Polar.pol(cur.recform);
                dataTable.put(storingName, cur);
            }
            case "equation" -> {
                String equals = queries.removeFirst();
                Vector cur = new Vector(0, 0, 0, 0);
                boolean sign = false;
                boolean minus = false;
                while (!queries.isEmpty()) {
                    String s = queries.removeFirst();
                    if (!sign) {
                        boolean skip = false;
                        if (s.charAt(0) == '-') {
                            minus ^= true;
                            s = s.substring(1);
                        }

                        String decipart = "";
                        while ((s.charAt(0) >= '0') && (s.charAt(0) <= '9')) {
                            decipart += s.charAt(0);
                            s = s.substring(1);
                        }
                        if (s.charAt(0) == '.') {
                            decipart += s.charAt(0);
                            s = s.substring(1);
                            while (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                                decipart += s.charAt(0);
                                s = s.substring(1);
                            }
                        }
                        double scalar = 1;
                        if (!decipart.isEmpty()) {
                            scalar = Double.parseDouble(decipart);
                        }

                        if (s.isEmpty()) {
                            Voicelines.errorBadName();
                            return;
                        }

                        if (!minus) {
                            cur.add(dataTable.get(s).scale(scalar));
                        } else {
                            cur.subtract(dataTable.get(s).scale(scalar));
                        }
                        minus = false;
                    } else {
                        minus = (s.equals("-"));
                    }
                    sign ^= true;
                }
                dataTable.put(storingName, cur);
            }
            default -> {
                Voicelines.errorStorageType(storingType);
                return;
            }
        }

        Voicelines.stored(storingName);
    }
    
    private void calc(){
        //calculations for velocity and acceleration goes here
    }

    private void desmos(){
        StringBuilder output = new StringBuilder();
        for (String query : queries) {
            if (!dataTable.containsKey(query)) {
                Voicelines.errorNonexistentVector(query);
                continue;
            }
            Vector cur = dataTable.get(query);
            double d = cur.polform.getTheta().getRad(), a = Math.PI / 4 - d, r = cur.polform.getMag();
            output.append(String.format("y\\cos %f+x\\sin %f=\\left(x\\cos %f-y\\sin %f\\right)\\left\\{y\\%ce0\\right\\}\\left\\{x^{2}+y^{2}\\le%f^{2}\\right\\}\n", a, a, a, a, (d <= Math.PI ? 'g' : 'l'), r));
            output.append(String.format("V_{%s}=\\ \\left(%s\\cos %s,%s\\sin %s\\right)\n",query,r,d,r,d));
        }
        Voicelines.desmos();
        System.out.println(output);
    }

    private void retrieve(){
        String name = queries.removeFirst();
        String type = queries.removeFirst();
        switch(type){
            case "polar" -> Vector.printPol(dataTable.get(name));
            case "rect" ->  Vector.printRec(dataTable.get(name));
            case "both" ->  Vector.print(dataTable.get(name));
            default -> Voicelines.retrievalTypeError(type);
        }
    }

    private void thank(){
        Voicelines.thank();
    }

    private void list(){
        Voicelines.listVector();
        System.out.println(dataTable.keySet());
    }

    private void unknownQuery(){
        Voicelines.queryNotRecognised();
    }

    private void set(){
        String settingName = queries.removeFirst();
        switch(settingName){
            case "precision" -> changePrecision();
            case "style" -> changeStyle();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(queries.removeFirst());
        Settings.setPrecision(precision);
        Voicelines.changeSetting("precision",Integer.toString(precision));
    }

    private void alias(){
        String newalias = queries.removeFirst();
        String to = queries.removeFirst();
        aliases.put(newalias,to);
    }

    private void unalias(){
        String removal = queries.removeFirst();
        if (aliases.containsKey(removal)) {
            queries.remove(removal);
        }
    }

    private void changeStyle(){
        String style = queries.removeFirst();
        Settings.setStyle(style);
        Voicelines.changeSetting("style",style);
    }

    private void changeInputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setInputAngleFormat(angleformat);
        Voicelines.changeSetting("the input angle format",angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setOutputAngleFormat(angleformat);
        Voicelines.changeSetting("the output angle format",angleformat);
    }

    private void debug(){
        Voicelines.debug();
    }
}
