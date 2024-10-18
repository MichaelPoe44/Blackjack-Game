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
    int numberOfUsers = 0;
    int locationOfUser;


    public Data(){
        try {
            file = new File("data.txt");
            
            // checks if file exists
            if (file.exists() && file.isFile()){

                fr = new FileReader(file);
                br = new BufferedReader(fr);
                br.mark(1000);
                
        

                while (br.readLine() != null) numberOfUsers++;
                br.reset();

                //recording all data
                for (int i=0; i < numberOfUsers; i++){

                    String temp = br.readLine();
                    allData += temp;
                    allData += "\n";

                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //returns if the string is in file so can make the text into a user
    public boolean userInFile(String text){
        //gets number of total users
        try {
                
            
            br.reset();
            
            
            if (numberOfUsers > 0){
                
                int lengthOfUsername = text.length();

                //recording all data that is not the current player and getting money for the current player
                for (int i=0; i < numberOfUsers; i++){

                    String temp = br.readLine();
                    
                    // makes sure length of current line is longer than username to avoid index error
                    if (temp.length() >= lengthOfUsername){
                        if (temp.substring(0,lengthOfUsername).equals(text)){
                            locationOfUser = i; //records location of user in file
                            return true;
                        }
                        
                    }
                }

            }
        }

        catch(Exception e){
            e.printStackTrace();
            
        }
        return false;
    }

    
    public void getData(Player user){
        //gets number of total users
        try {
            allData = "";
            br.reset();
            int lengthOfUsername = user.Username.length();

            //recording all data that is not the current player and getting money for the current player
            for (int i=0; i < numberOfUsers; i++){

                String temp = br.readLine();
                    
                
                if (i == locationOfUser){
                    int money = Integer.valueOf(temp.substring(lengthOfUsername+1));
                    user.setMoney(money);
                }

                else {
                    allData += temp;
                    allData += "\n";
                }
                    
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    
    







    public void makeNewUser(Player user){
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
        // if window closed before user given
        catch (Exception e){
            try {
                fw = new FileWriter(file);
                System.out.println("closed before user given");
                fw.write(allData);
                fw.close();
                
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
        
    }

}