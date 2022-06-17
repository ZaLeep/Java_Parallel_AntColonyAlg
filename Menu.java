import java.util.Scanner;

public class Menu {
    private final static String MAIN_MENU = """
            Enter number to choose option:
            1) Sequential algorithm
            2) Concurrent algorithm
            3) Exit
            >""";
    private final static String OUT_MENU = """
            Enter number  to choose output type:
            1) Only total quality info
            2) Every try quality info
            3) +Paths
            4) +Matrices
            5) *Back
            >""";
    private final static String[] INP_MENU = {  "Enter number of \"cities\":",
                                        "Enter number of ants:",
                                        "Enter number of threads:",
                                        "Enter number of attepmts:"};
    private final static String ERROR_MSG = "Wrong input. Try again.\n";
    private final static String RETURN = "Press ENTER to return...";
    private final static String[][] OUT_INFO = {{"Average time: ", "Average quality: "},
                                        {"Search time: ", "Search quality: "},
                                        {"Best path:", "Lenght: "},
                                        {"Start weights and feramones:", "Final weights and feramones:"}};
    private final static String NEXT_ATTEMPT = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
    private final static String CLEAR_PAGE = "\033[H\033[J";
    private final static Scanner sc = new Scanner(System.in);
    
    public static void mainMenu() {
        boolean exitFlag = true;
        while(exitFlag) {
            System.out.print(CLEAR_PAGE);
            System.out.print(MAIN_MENU);
            switch(sc.nextLine().charAt(0)) {
                case '1':
                    System.out.print(CLEAR_PAGE);
                    algMenu(false);
                    break;
                case '2':
                    System.out.print(CLEAR_PAGE);
                    algMenu(true);
                    break;
                case '3':
                    System.out.print(CLEAR_PAGE);
                    exitFlag = false;
                    sc.close();
                    break;
                default:
                    System.out.print(CLEAR_PAGE);
                    System.out.print(ERROR_MSG);
                    System.out.print(RETURN);
                    sc.nextLine();
            }
        }
    }

    private static void algMenu(boolean isParallel) {
        int outType = outTypeMenu();
        if(outType < 5) {
            int[] algConfig = inputMenu(isParallel);
            System.out.print(CLEAR_PAGE);
            ISwarm preSwarm = (isParallel) ? new ModernSwarm(algConfig[1], new Land(algConfig[0]), algConfig[2]) : new Swarm(algConfig[1], new Land(algConfig[0]));
            preSwarm.search();

            long sumTime = 0;
            double sumQuality = 0;
            for(int i = 0; i < algConfig[3]; i++) {
                ISwarm swarm = (isParallel) ? new ModernSwarm(algConfig[1], new Land(algConfig[0]), algConfig[2]) : new Swarm(algConfig[1], new Land(algConfig[0]));
                if(outType > 1) {
                    System.out.println("Attempt #" + (i + 1) + ":");
                    if(outType == 4) {
                        System.out.println(OUT_INFO[3][0]);
                        System.out.println(swarm.getLand());
                    }
                }

                swarm.search();
                sumTime += swarm.getTR();
                sumQuality += swarm.getQR();

                if(outType > 1) {
                    if(outType == 4) {
                        System.out.println(OUT_INFO[3][1]);
                        System.out.println(swarm.getLand());
                    }
                    if(outType > 2) {
                        System.out.println(OUT_INFO[2][0]);
                        System.out.println(swarm.pathToString());
                        System.out.println(OUT_INFO[2][1] + swarm.getLmin());
                    }
                    if(outType > 1) {
                        System.out.println(OUT_INFO[1][0] + swarm.getTR() + "ms");
                        System.out.println(OUT_INFO[1][1] + swarm.getQR());
                    }
                    System.out.println(NEXT_ATTEMPT);
                }
            }
            long avgTime = sumTime / algConfig[3];
            double avgQuality = sumQuality / algConfig[3];
            System.out.println(OUT_INFO[0][0] + avgTime + "ms");
            System.out.println(OUT_INFO[0][1] + avgQuality);
            System.out.print(RETURN);
            sc.nextLine();
        }
    }

    private static int outTypeMenu() {
        boolean exitFlag = true;
        while(exitFlag) {
            System.out.print(CLEAR_PAGE);
            System.out.print(OUT_MENU);
            switch(sc.nextLine().charAt(0)) {
                case '1':
                    return 1;
                case '2':
                    return 2;
                case '3':
                    return 3;
                case '4':
                    return 4;
                case '5':
                    return 5;
                default:
                    System.out.print(CLEAR_PAGE);
                    System.out.print(ERROR_MSG);
                    System.out.print(RETURN);
                    sc.nextLine();
            }
        }
        System.out.println("Unexpected error in selecting type of output(");
        return 5;
    }

    private static int[] inputMenu(boolean isParallel) {
        int[] result = {-1, -1, -1, -1};
        System.out.print(CLEAR_PAGE);
        
        for(int i = 0; i < 4; i++) {
            if(i != 2 || (i == 2 && isParallel)) {
                boolean rightInput = false;
                while(!rightInput) {
                    System.out.print(INP_MENU[i] + "\n>");
                    String inp = sc.nextLine();
                    if(isInteger(inp)){
                        result[i] = Integer.parseInt(inp);
                        rightInput = true;
                    }
                    else {
                        System.out.print(ERROR_MSG);
                    }
                }
            }
        }
        return result;
    }

    private static boolean isInteger(String test) {
        for(int i = 0; i < test.length(); i++) {
            int n = (int)test.charAt(i);
            if(n < 48 || n > 57)
                return false;
        }
        if(test.charAt(0) == '0')
            return false;
        return true;
    }
}
