package com.example.a4;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.util.Pair;

public class Doll extends View{

    public Doll(Context context, AttributeSet attrs){
        super(context,attrs);
        set_doll();
        setOnTouchListener(new Actionlistener());
        scaling = new ScaleGestureDetector(context,new Scalelistner());

    }

    public void reset(){
        cur_body = null;
        set_doll();
        this.invalidate();
    }

    Body torso;
    Point drag_pt = null;
    Body cur_body;
    Point body_pt;
    Pair<Integer,Integer> cur_distance = new Pair<>(0, 0);
    ScaleGestureDetector scaling;
    float scale_to = 1;


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        torso.draw(canvas);

    }

   public class Scalelistner extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector){
            System.out.print("scaling begin");
            Point cur_pt = new Point((int)scaleGestureDetector.getFocusX(),(int)scaleGestureDetector.getFocusY());
            cur_body = torso.getScalehit(cur_pt);
            if(cur_body==null||(cur_body.type!=Body.Type.upleg&&cur_body.type!=Body.Type.downleg)){
                return false;
            }else{
                return true;
            }
        }
        @Override
       public boolean onScale(ScaleGestureDetector s){
            if(cur_body==null){
                System.out.print("not scaling");
                return false;
            }
            scale_to = s.getScaleFactor()*scale_to;
            if(scale_to>3){
                scale_to=3;
            } else if(scale_to<0.5){
                scale_to = 0.5f;
            }
            if(cur_body.type==Body.Type.upleg){
                cur_body.scale = scale_to;
                cur_body.children.get(0).children.get(0).scale = 1/scale_to;
            } else if(cur_body.type==Body.Type.downleg){
                cur_body.scale = scale_to;
                cur_body.children.get(0).scale = 1/scale_to;
            }
            invalidate();
            return true;

        }

   }

    private class Actionlistener implements OnTouchListener{
        public boolean onTouch(View view, MotionEvent motionEvent){
            scaling.onTouchEvent(motionEvent);
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    handle_down(motionEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(cur_body!=null){
                        System.out.println("body is "+cur_body.type);
                    }
                    if(cur_body==null){
                        cur_body = torso.getBodyHit(motionEvent);
                    }

                    if(cur_body!=null) {
                        //System.out.println("here");
                        if (cur_body.type == Body.Type.torso) {
                            Point new_position = new Point((int) motionEvent.getX() - cur_distance.first,
                                    (int) motionEvent.getY() - cur_distance.second);
                            torso.setPosition(new_position);
                            torso.real_pt = torso.position;
                            cur_body.change_position();
                            view.invalidate();

                        } else if (cur_body.type == Body.Type.head) {
                            int direction = 1;
                            if (motionEvent.getX() < cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.head);
                            //System.out.println("angle before is "+ cur_body.rotate_angle);
                            if (cur_deg > 50) cur_deg = 50;
                            cur_body.rotate_angle = direction * cur_deg;
                            //System.out.println("after is "+ cur_body.rotate_angle);
                            view.invalidate();


                        } else if (cur_body.type == Body.Type.uparm) {
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.uparm);
                            cur_body.rotate_angle = cur_deg;
                            //cur_body.change_position();
                            view.invalidate();
                        } else if (cur_body.type == Body.Type.upleg) {
                            int direction = 1;
                            if (motionEvent.getX() > cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.upleg);
                            if (cur_deg > 90) cur_deg = 90;
                            cur_body.rotate_angle = direction * cur_deg;
                            view.invalidate();

                        } else if (cur_body.type == Body.Type.downarm) {
                            int direction = 1;
                            if (motionEvent.getX() > cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.downarm);
                            if (cur_deg > 135) cur_deg = 135;
                            cur_body.rotate_angle = direction * cur_deg;
                            //handle_sub_child(motionEvent);
                            view.invalidate();
                        } else if (cur_body.type == Body.Type.downleg) {
                            int direction = 1;
                            if (motionEvent.getX() > cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.downleg);
                            if (cur_deg > 90) cur_deg = 90;
                            cur_body.rotate_angle = direction * cur_deg;
                            view.invalidate();
                        } else if (cur_body.type == Body.Type.hand) {
                            int direction = 1;
                            if (motionEvent.getX() > cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.downarm);
                            if (cur_deg > 35) cur_deg = 35;
                            cur_body.rotate_angle = direction * cur_deg;
                            view.invalidate();

                        } else if (cur_body.type == Body.Type.leftfeet||cur_body.type == Body.Type.rightfeet) {
                            int direction = 1;
                            if (motionEvent.getX() > cur_body.real_pt.x) {
                                direction = -direction;
                            }
                            Point cur_pt = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                            float cur_deg = get_angle(cur_body.real_pt, cur_pt, Body.Type.downleg);
                            if (cur_deg > 35) cur_deg = 35;
                            cur_body.rotate_angle = direction * cur_deg;
                            view.invalidate();

                        }
                    }
                    break;

            }
            return true;


        }
    }






    public void handle_down(MotionEvent e){
        cur_body = torso.getBodyHit(e);
        if(cur_body!=null){
            drag_pt = new Point((int)e.getX(),(int)e.getY());
            body_pt = cur_body.position;
            cur_distance = Pair.create(drag_pt.x-body_pt.x,drag_pt.y-body_pt.y);

        }
    }


    public float get_angle(Point axis, Point my_pt,Body.Type t){
        //System.out.print("axis is " + axis.x + ' ' +axis.y);
        //System.out.print("my_pt is " + my_pt.x + ' ' +my_pt.y);
        double rel_x = axis.x - my_pt.x;
        double rel_y = axis.y - my_pt.y;

        /*if(axis.y<my_pt.y){
            rel_y = -rel_y;
        }*/

        float deg =(float)Math.toDegrees(Math.atan2(rel_y,rel_x));
        if(t==Body.Type.head){
            if(my_pt.x>axis.x){
                deg-=90;
            }else{
                deg-=90;
                deg = -deg;
            }
        } else if(t==Body.Type.uparm){
            if(my_pt.x<axis.x){
                deg-=270;
            } else {
                deg-=270;
            }
        }else if(t==Body.Type.upleg) {
            if (my_pt.x < axis.x) {
                deg -= 270;
            } else {
                deg -= 270;
                deg = -deg;
            }
        }else if(t==Body.Type.downarm){
            if (my_pt.x < axis.x) {
                deg -= 270;
            } else {
                deg -= 270;
                deg = -deg;
            }
        }else if(t==Body.Type.downleg){
            if (my_pt.x < axis.x) {
                deg -= 270;
            } else {
                deg -= 270;
                deg = -deg;
            }
        }




        if(deg<0){
            deg+=360;
        }else if(deg>360){
            deg-=360;
        }
       // System.out.print("degree is " + deg);
        return deg;
    }



    public void set_doll(){
        torso = new Body(Body.Type.torso);
        torso.setPosition(new Point(600,600));

        Body head = new Body(Body.Type.head);
        head.setPosition(new Point(100,-15));

        Body upleftarm = new Body(Body.Type.uparm);
        upleftarm.setPosition(new Point(-15,0));

        Body downleftarm = new Body(Body.Type.downarm);
        downleftarm.setPosition(new Point(0,200));

        Body lefthand = new Body(Body.Type.hand);
        lefthand.setPosition(new Point(0,155));

        Body uprightarm = new Body(Body.Type.uparm);
        uprightarm.setPosition(new Point(215,0));

        Body downrightarm = new Body(Body.Type.downarm);
        downrightarm.setPosition(new Point(0,200));

        Body righthand = new Body(Body.Type.hand);
        righthand.setPosition(new Point(0,155));

        Body upleftleg = new Body(Body.Type.upleg);
        upleftleg.setPosition(new Point(60,320));

        Body downleftleg = new Body(Body.Type.downleg);
        downleftleg.setPosition(new Point(0,220));

        Body leftfeet = new Body(Body.Type.leftfeet);
        leftfeet.setPosition(new Point(0,240));

        Body uprightleg = new Body(Body.Type.upleg);
        uprightleg.setPosition(new Point(140,320));

        Body downrightleg = new Body(Body.Type.downleg);
        downrightleg.setPosition(new Point(0,220));

        Body rightfeet = new Body(Body.Type.rightfeet);
        rightfeet.setPosition(new Point(0,240));

        torso.addChild(head);
        torso.addChild(upleftarm);
        upleftarm.addChild(downleftarm);
        upleftarm.setAngle(12);
        downleftarm.addChild(lefthand);
        downleftarm.setAngle(-12);
        torso.addChild(uprightarm);
        uprightarm.addChild(downrightarm);
        uprightarm.setAngle(-12);
        downrightarm.addChild(righthand);
        downrightarm.setAngle(12);

        torso.addChild(upleftleg);
        upleftleg.addChild(downleftleg);
        downleftleg.addChild(leftfeet);
        torso.addChild(uprightleg);
        uprightleg.addChild(downrightleg);
        downrightleg.addChild(rightfeet);

        torso.initial_real_pt();






    }
}
