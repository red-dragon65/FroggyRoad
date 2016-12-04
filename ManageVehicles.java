import java.util.Random;



class ManageVehicles {


    //Create random generator.
    private Random rand = new Random();



    /**
     * Method that creates and resets cars on the road strip.
     */
    Sprite setCar(int stripYLoc) {

        //Makes sprite.
        Sprite car = new Sprite();

        //Scrolls sprite.
        car.setYDir(2);

        //Set sprite to strip location.
        car.setYLoc(stripYLoc);

        if (rand.nextInt(2) == 1) {
            //Right to left.
            car.setXLoc(900);
            car.setXDir(-(rand.nextInt(10) + 10));
            car.setImage(randomCar("left"));

        } else {
            //Left to right.
            car.setXLoc(-200);
            car.setXDir((rand.nextInt(10) + 10));
            car.setImage(randomCar("right"));
        }

        return car;
    }


    /**
     * Method to return random car color.
     */
    String randomCar(String dir) {

        //Car color variables.
        int carColor = rand.nextInt(8);
        String carImage = "";

        if (dir.equals("left")) {

            switch (carColor) {
                case 0:
                    carImage = "/Car_Left/Car_Left_Blue.png";
                    break;
                case 1:
                    carImage = "/Car_Left/Car_Left_Green.png";
                    break;
                case 2:
                    carImage = "/Car_Left/Car_Left_Grey.png";
                    break;
                case 3:
                    carImage = "/Car_Left/Car_Left_Orange.png";
                    break;
                case 4:
                    carImage = "/Car_Left/Car_Left_Purple.png";
                    break;
                case 5:
                    carImage = "/Car_Left/Car_Left_Red.png";
                    break;
                case 6:
                    carImage = "/Car_Left/Car_Left_White.png";
                    break;
                case 7:
                    carImage = "/Car_Left/Car_Left_Yellow.png";
                    break;
            }
        }

        if (dir.equals("right")) {

            switch (carColor) {
                case 0:
                    carImage = "/Car_Right/Car_Right_Blue.png";
                    break;
                case 1:
                    carImage = "/Car_Right/Car_Right_Green.png";
                    break;
                case 2:
                    carImage = "/Car_Right/Car_Right_Grey.png";
                    break;
                case 3:
                    carImage = "/Car_Right/Car_Right_Orange.png";
                    break;
                case 4:
                    carImage = "/Car_Right/Car_Right_Purple.png";
                    break;
                case 5:
                    carImage = "/Car_Right/Car_Right_Red.png";
                    break;
                case 6:
                    carImage = "/Car_Right/Car_Right_White.png";
                    break;
                case 7:
                    carImage = "/Car_Right/Car_Right_Yellow.png";
                    break;
            }
        }

        return carImage;
    }




    /**
     * Method that creates and resets trains on the track strip.
     */
    Sprite setTrain(int stripYLoc) {

            //Makes sprite.
            Sprite train = new Sprite(randomTrain());

            //Scrolls sprite.
            train.setYDir(2);

            //Set sprite to strip location.
            train.setYLoc(stripYLoc);

            if (rand.nextInt(2) == 1) {
                //Right to left.
                train.setXLoc(900);
                train.setXDir(-(rand.nextInt(10) + 30));
            } else {
                //Left to right.
                train.setXLoc(-1500);
                train.setXDir((rand.nextInt(10) + 30));
            }

            return train;


    }

    /**
     * Method to return a random colored train.
     */
    String randomTrain() {

        int trainNum = rand.nextInt(10);
        String trainImage = "";


        switch (trainNum) {
            case 0:
                trainImage = "/Trains/Train_Blue.png";
                break;
            case 1:
                trainImage = "/Trains/Train_Green.png";
                break;
            case 2:
                trainImage = "/Trains/Train_Grey.png";
                break;
            case 3:
                trainImage = "/Trains/Train_Orange.png";
                break;
            case 4:
                trainImage = "/Trains/Train_Purple.png";
                break;
            case 5:
                trainImage = "/Trains/Train_Red.png";
                break;
            case 6:
                trainImage = "/Trains/Train_White.png";
                break;
            case 7:
                trainImage = "/Trains/Train_Yellow.png";
                break;
        }

        return trainImage;
    }


}
