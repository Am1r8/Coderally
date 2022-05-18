import com.ibm.jc.JavaChallenge;
import com.ibm.rally.Car;
import com.ibm.rally.ICar;
import com.ibm.rally.IObject;
import com.ibm.rally.World;
import java.awt.Point;

@JavaChallenge(name = "Amir", organization = "AlPHA Nator")
public class RallyCar extends Car {
        protected static final int WAIT = 100;
        private Point[] wayPoints;
        private int nextWayPoint;
        private Point[] wayPoints1;
        private int nextWayPoint1;
        private int waitLeft;

        public void initialize() {
            IObject[] fuel = World.getFuelDepots();
            IObject[] check = World.getCheckpoints();
            int size = check.length;
            int size2 = fuel.length;
            
            this.wayPoints1 = new Point[size2];
            for (int i = 0; i < size2; ++i) {
                this.wayPoints1[i] = new Point((int)fuel[i].getX(), (int)fuel[i].getY());
            }
            this.wayPoints = new Point[size];
            for (int i = 0; i < size; ++i) {
                this.wayPoints[i] = new Point((int)check[i].getX(), (int)check[i].getY());
            }
            this.nextWayPoint1 = 0;
            this.nextWayPoint = 0;
            this.waitLeft = 0;
            this.setThrottle(100);

        }
        protected int getWheelRotation(int heading, int targetHeading) {
            int distance_vertical = 2;
            if (Math.abs(heading - targetHeading) % 360 <= Math.abs(distance_vertical)) {
                return 0;
            }
            int distance_horiz = targetHeading - heading;
            if (distance_horiz > 180) {
                distance_horiz -= 360;
            } else if (distance_horiz < -180) {
                distance_horiz += 360;
            }
            if (distance_horiz > 0) {
                return 10;
            }
            return -10;
        }
        public void move(int lastMoveTime, boolean hitWall, ICar hitCar, ICar hitBySpareTire) {
            int time = 600 - World.getCurrentTurn();
            System.out.println(time);
            Point q = this.wayPoints1[this.nextWayPoint1];
            double j = this.getDistanceTo(q.x, q.y);
            int f = this.getFuel();
            if (j < 70.0 && this.waitLeft == 0) {
                if (f < 40) {
                    this.setThrottle(0);
                    this.waitLeft = 90;
                    this.enterProtectMode();
                } else {
                    this.setThrottle(100);
                }
            }
            if (this.waitLeft == 1) {
                ++this.nextWayPoint1;
                if (this.nextWayPoint1 >= this.wayPoints1.length) {
                    this.nextWayPoint1 = 0;
                }
                q = this.wayPoints[this.nextWayPoint1];
                j = this.getDistanceTo(q.x, q.y);
                this.setThrottle(100);
            }
            if (this.waitLeft > 0) {
                --this.waitLeft;
            }
            int k = this.getHeadingTo(q.x, q.y);
        
            Point p = this.wayPoints[this.nextWayPoint];
            double d = this.getDistanceTo(p.x, p.y);
            if (d < 30.0) {
                ++this.nextWayPoint;
                if (this.nextWayPoint >= this.wayPoints.length) {
                    this.nextWayPoint = 0;
                }
                p = this.wayPoints[this.nextWayPoint];
                d = this.getDistanceTo(p.x, p.y);
                this.setThrottle(100);
            }
            int h = this.getHeadingTo(p.x, p.y);
            if (time > 200) {
                if (f > 35) {
                    if (hitCar != null) {
                    this.enterProtectMode();
                    this.setThrottle(-10);
                    this.throwSpareTire();
                    this.waitLeft = 10;
                        if (this.waitLeft > 0) {
                            --this.waitLeft;
                        }
                        if (this.waitLeft == 0) {
                        this.setThrottle(100);
                        }
                    }
                this.setSteeringSetting(this.getWheelRotation(this.getHeading(), h));
                } else if(f < 40) {
                    this.setSteeringSetting(this.getWheelRotation(this.getHeading(), k));
                }
            } else {
                this.setSteeringSetting(this.getWheelRotation(this.getHeading(), h));
            }
        }

    @Override
    public byte getColor() {
        return CAR_TEAL;
    }
}