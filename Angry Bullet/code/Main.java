import java.awt.*;
import java.awt.event.KeyEvent;

/**
* @author enes.ozturk
 *Angry Bullet Game Project
 */

public class Main {
    public static void main(String[] args) {
        // Game Parameters
        int width = 1600; //screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
// Box coordinates for obstacles and targets
// Each row stores a box containing the following information:
// x and y coordinates of the lower left rectangle corner, width, and height
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };

        /**
         * myObstaclearray={
         * {200,0,80,300},
         * {500,50,60,90},
         * {1000,200,120,55},
         * {1300,300,30,200}
         * }
         *
         * myTargetArray={
         * {350,0,60,60},
         * {700,120,50,30},
         * {1200,50,40,80}
         * }
         */


        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(0,1600);
        StdDraw.setYscale(0,800);


        //Animation
        StdDraw.enableDoubleBuffering();
        int duration=1;  //Duration variable for animation part
        double time=0.0;  //time variable in the physical formula

        double ballX0=x0;  //ball's first x location
        double ballY0=y0;  //ball's first y location


        while (true){
            StdDraw.clear();  //clear the screen

            //Draw obstacles
            for (int i=0; i<obstacleArray.length; i++){
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                double xDirection=obstacleArray[i][0]+(obstacleArray[i][2]/2.0);
                double yDirection=obstacleArray[i][1]+(obstacleArray[i][3]/2.0);
                StdDraw.filledRectangle(xDirection,yDirection,obstacleArray[i][2]/2,obstacleArray[i][3]/2);
            }

            //Draw targets
            for (int i=0; i<targetArray.length; i++){
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                double xDirection=targetArray[i][0]+(targetArray[i][2]/2.0);
                double yDirection=targetArray[i][1]+(targetArray[i][3]/2.0);
                StdDraw.filledRectangle(xDirection,yDirection,targetArray[i][2]/2,targetArray[i][3]/2);
            }

            //Draw the shooting platform
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(60,60,60,60);



            //Keyboard events
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){  //press left, decrease velocity
                StdDraw.pause(75);
                bulletVelocity-=1.0;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){  //press right, increase velocity
                StdDraw.pause(75);
                bulletVelocity+=1.0;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)){  //press up, increase angle
                StdDraw.pause(75);
                bulletAngle+=1.0;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)){  //press down, decrease angle
                StdDraw.pause(75);
                bulletAngle-=1.0;
            }


            //Draw the shooting line
            double lengthLine=bulletVelocity*bulletVelocity/425;  //setting shooting line length
            bulletAngle*=Math.PI/180;  //convert angle from integer to radian
            double x1=x0+(lengthLine*Math.cos(bulletAngle));  //the end x coordinate of the shooting line
            double y1=y0+(lengthLine*Math.sin(bulletAngle));  //the end y coordinate of the shooting line
            bulletAngle*=180/Math.PI;  //convert angle from radian to integer
            StdDraw.setPenRadius(0.008);  //setting pen radius to draw the shooting line
            StdDraw.setPenColor(StdDraw.BLACK);  //setting shooting line color
            StdDraw.line(x0,y0,x1,y1);


            //Locate the text of velocity and angle
            String angle=String.format("a: %.1f",bulletAngle);  //shooting platform bullet's angle text
            String velocity=String.format("v: %.1f",bulletVelocity);  //shooting platform bullet's velocity text
            StdDraw.setPenColor(StdDraw.WHITE);  //setting pen color
            StdDraw.setFont(new Font("Serif", Font.PLAIN, 18));
            StdDraw.text(50,40,velocity);
            StdDraw.text(50,80,angle);

            StdDraw.show();

            //Projectile Motion
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){  //when press the space ball's motion will start
                double ballX1;  //the x coordinate of the ball in next step
                double ballY1;  //the y coordinate of the ball in next step
                double angleRadian=Math.toRadians(bulletAngle);  //convert the angle from integer to radian


                String text="";  //set the text which is seen after game ends and also it will change according to conditions

                //Ball's motion
                while (true){
                    StdDraw.setPenColor(StdDraw.BLACK);  //set pen color
                    StdDraw.setPenRadius(0.002);  //set pen radius for line between two ball
                    ballX1=x0+bulletVelocity/1.7244*Math.cos(angleRadian)*time;
                    ballY1=y0+bulletVelocity/1.7244*Math.sin(angleRadian)*time-(1.0/2)*time*time*gravity;
                    StdDraw.filledCircle(ballX1,ballY1,4.0);
                    StdDraw.line(ballX0,ballY0,ballX1,ballY1);
                    ballX0=ballX1;  //swap the last x coordinate and new x coordinate
                    ballY0=ballY1;  //swap the last y coordinate and new y coordinate
                    time+=0.2;  //time increase


                    //Conditions
                    boolean control=false;  //control variable to finish game when ball strike an obstacle or target or bottom

                    for (int i=0; i<obstacleArray.length; i++){  //control whether the ball strike an obstacle
                        if ((ballX0<=obstacleArray[i][0]+obstacleArray[i][2]) && (ballX0>=obstacleArray[i][0]) && (ballY0<=obstacleArray[i][3]+obstacleArray[i][1]) && (ballY0>=obstacleArray[i][1])){
                            control=true;
                            text="Hit an obstacle. Press 'r' to shoot again.";
                            break;
                        }
                    }

                    if (control==false){
                        for (int i=0; i<targetArray.length; i++){  //control whether the ball hit a target
                            if ((ballX0<=targetArray[i][0]+targetArray[i][2]) && (ballX0>=targetArray[i][0]) && (ballY0<=targetArray[i][3]+targetArray[i][1]) && (ballY0>=targetArray[i][1])){
                                control=true;
                                text="Congratulations: You hit the target!";
                                break;
                            }
                        }
                    }

                    if (ballX0>1600){  //control whether the ball's x coordinate exceed the frame width
                        control=true;
                        text="Max X reached. Press 'r' to shoot again.";
                    }

                    if (ballY0<=0){  //control whether the ball hit the ground
                        control=true;
                        text="Hit the ground. Press 'r' to shoot again.";
                    }

                    StdDraw.show();  //draw the animation
                    StdDraw.pause(duration);

                    if (control){  //one of the above conditions happened if block break the animation loop
                        break;
                    }



                }
                //Write the text on the screen
                StdDraw.setFont(new Font("Helvetica",Font.BOLD,22));
                StdDraw.textLeft(10,780,text);
                StdDraw.show();

                //Check if player type "r" or not
                while (true){
                    if (StdDraw.isKeyPressed(KeyEvent.VK_R)){
                        StdDraw.pause(100);
                        ballX0=0;
                        ballY0=0;
                        bulletAngle=45.0;
                        bulletVelocity=180.0;
                        time=0.0;
                        break;
                    }
                }

            }

        }


    }
}
