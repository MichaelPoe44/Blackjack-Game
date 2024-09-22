import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;



class Data{

    File file;
    FileWriter fw;
    FileReader fr;
    BufferedReader br;

    String allData = "";
    boolean userInFile = false;
    int numberOfUsers = 0;


    public Data(Player user){
        try {
            file = new File("data.txt");
            
            // checks if file exists
            if (file.exists() && file.isFile()){

                fr = new FileReader(file);
                br = new BufferedReader(fr);
                br.mark(1000);

                //gets number of total users
                while (br.readLine() != null) numberOfUsers++;
                br.reset();
                

                
                if (numberOfUsers > 0){
                    
                    int lengthOfUsername = user.Username.length();

                    //recording all data that is not the current player and getting money for the current player
                    for (int i=0; i < numberOfUsers; i++){

                        String temp = br.readLine();
                        
                        if (temp.length() >= lengthOfUsername){
                            if (temp.substring(0,lengthOfUsername).equals(user.Username)){

                                int money = Integer.valueOf(temp.substring(lengthOfUsername+1));
                                user.setMoney(money);
                                userInFile = true;

                            }
                            else{
                                allData += temp;
                                allData += "\n";

                            }
                        }
                        else {
                            allData += temp;
                            allData += "\n";
                        }

                        
                    }


                }
                
                // if player not in list gives them default money
                if (!userInFile) deafualt(user);
                
                

            }
            //if no file give player defualt money
            else{
                deafualt(user);
            }
            


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    public void deafualt(Player user){
        user.setMoney(500);
    }



    public void endGame(Player user){
        try{
            fw = new FileWriter(file);

            fw.write(allData);
            fw.write(user.Username);
            fw.write(" "+String.valueOf(user.money));
            fw.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }

}