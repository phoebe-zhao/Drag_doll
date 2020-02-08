package com.example.a4;
import java.lang.reflect.Type;
import java.util.Vector;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class Body {
    Type type;
    Vector<Body> children = new Vector<Body>();
    Body parent;
    Point position;
    float rotate_angle = 0;
    float scale = 1;
    RectF rectf;
    Point real_pt = new Point(0,0);


    public enum Type {
        torso,
        head,
        uparm,
        downarm,
        hand,
        upleg,
        downleg,
        leftfeet,
        rightfeet
    }

    public Body(Type t){
        type = t;
    }

    public void addChild(Body b){
        children.add(b);
        b.setParent(this);
    }

    public void setParent(Body b){
        this.parent = b;
    }

    public void setPosition(Point p){
        position = p;
    }

    public void setAngle(float angle){
        rotate_angle = angle;

    }

    public Body getBodyHit(MotionEvent e){
        for(Body body:children){
            Body b = body.getBodyHit(e);
            if(b!=null) {
                return b;
            }
        }
        Point new_p = new Point((int)e.getX(),(int)e.getY());
        Point contain_p = new Point(new_p.x-real_pt.x,new_p.y-real_pt.y);
        /*if(this.type==Type.torso){
            System.out.println("torso position is " + position.x + " y is " + position.y);
            System.out.println("torso real_pt is " + real_pt.x + " y is " + real_pt.y);
        }
        if(this.type==Type.head){
            System.out.println("head real_pt is " + real_pt.x + " y is " + real_pt.y);
            System.out.println("contain p x is " + contain_p.x + " y is " + contain_p.y);
            if(rectf.contains(contain_p.x,contain_p.y)){
                System.out.println("contain head");;
            }
        }
        if(this.type==Type.torso){
            System.out.println("torso contain p x is " + contain_p.x + " y is " + contain_p.y);
        }*/

        /*if(this.type==Type.head){
            System.out.println("head real_pt is " + real_pt.x + " y is " + real_pt.y);
            System.out.println("contain p x is " + contain_p.x + " y is " + contain_p.y);
            if(rectf.contains(contain_p.x,contain_p.y)){
                System.out.println("contain head");;
            }
        }*/
        /*if(this.type==Type.downarm) {
            //System.out.println("downarm real_pt is " + real_pt.x + " y is " + real_pt.y);
            System.out.println("contain p before x is " + contain_p.x + " y is " + contain_p.y);
        }*/
        if(this.type==Type.torso){
            System.out.println("torso real is " + real_pt.x + " y is " + real_pt.y);
            System.out.println("actual point is " + new_p.x + " y is " + new_p.y);
        }

        if(rotate_angle!=0){
            double radius_angle = Math.toRadians(rotate_angle);
            if(parent!=null&&parent.rotate_angle!=0/*&&(parent.rotate_angle==12||parent.rotate_angle==-12)*/){
                //System.out.println("parent rotate angle is " + parent.rotate_angle);
                double radius_parent = Math.toRadians(parent.rotate_angle);
                Point temp_real = new Point((int)(position.x*Math.cos(radius_parent)-position.y*Math.sin(radius_parent)),
                        (int)(position.x*Math.sin(radius_parent)+position.y*Math.cos(radius_parent)));
                if(this.type==Type.downarm){
                    System.out.println("temp real before is " + temp_real.x + " y is " + temp_real.y);
                }
                temp_real.x += parent.real_pt.x;
                temp_real.y += parent.real_pt.y;

                contain_p = new Point(new_p.x-temp_real.x,new_p.y-temp_real.y);
                if(this.type==Type.downarm){
                    System.out.println("parent real is " + parent.real_pt.x + " y is " + parent.real_pt.x);
                    System.out.println("point is " + new_p.x + " y is " + new_p.y);
                    System.out.println("temp real is " + temp_real.x + " y is " + temp_real.y);
                    System.out.println("torso contain p after is " + contain_p.x + " y is " + contain_p.y);
                }


            }
            /*if(this.type==Type.uparm){
                //System.out.println("real is " + real_pt.x + " y is " + real_pt.y);
                System.out.println("point is " + new_p.x + " y is " + new_p.y);
                //System.out.println("uparm real is " + temp_real.x + " y is " + temp_real.y);
                //System.out.println("torso contain p after is " + contain_p.x + " y is " + contain_p.y);
            }*/
            contain_p = new Point((int)(contain_p.x*Math.cos(-radius_angle)-contain_p.y*Math.sin(-radius_angle)),
                    (int)(contain_p.x*Math.sin(-radius_angle)+contain_p.y*Math.cos(-radius_angle)));
            if(this.type==Type.downarm){
                System.out.println("torso contain p final is " + contain_p.x + " y is " + contain_p.y);
            }
            /*if(this.type==Type.uparm){
                System.out.println("uparm contain p final is " + contain_p.x + " y is " + contain_p.y);
            }*/
        }
        if(rectf.contains(contain_p.x,contain_p.y)){
            return this;
        }
        return null;
    }


    public Body getScalehit(Point p){
            for(Body body:children){
                Body b = body.getScalehit(p);
                if(b!=null) {
                    return b;
                }
            }
            Point new_p = p;
            Point contain_p = new Point(new_p.x-real_pt.x,new_p.y-real_pt.y);

            if(rotate_angle!=0){
                double radius_angle = Math.toRadians(rotate_angle);
                if(parent!=null&&parent.rotate_angle!=0/*&&(parent.rotate_angle==12||parent.rotate_angle==-12)*/){
                    //System.out.println("parent rotate angle is " + parent.rotate_angle);
                    double radius_parent = Math.toRadians(parent.rotate_angle);
                    Point temp_real = new Point((int)(position.x*Math.cos(-radius_parent)-position.y*Math.sin(radius_parent)),
                            (int)(position.x*Math.sin(-radius_parent)+position.y*Math.cos(-radius_parent)));
                    temp_real.x += parent.real_pt.x;
                    temp_real.y += parent.real_pt.y;

                    contain_p = new Point(new_p.x-temp_real.x,new_p.y-temp_real.y);

                }
                contain_p = new Point((int)(contain_p.x*Math.cos(-radius_angle)-contain_p.y*Math.sin(-radius_angle)),
                        (int)(contain_p.x*Math.sin(-radius_angle)+contain_p.y*Math.cos(-radius_angle)));
            }
        rectf.bottom*=scale;
            if(rectf.contains(contain_p.x,contain_p.y)){
                return this;
            }
            return null;
    }


    public void change_position(){
        for(Body body:children){
            Point new_position = new Point((int)body.position.x+real_pt.x,
                    (int)body.position.y + real_pt.y);
            body.real_pt = new_position;
            if(body.type==Type.downarm){

            }
            body.change_position();
        }
    }


    public void initial_real_pt(){
        real_pt = position;
        for(Body body:children){
            body.initial_real_pt();
        }
    }


    public void draw(Canvas canvas){
        canvas.save();
        canvas.translate(position.x,position.y);
        canvas.rotate(rotate_angle);
        canvas.scale(1,scale);
        Paint cur_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cur_paint.setStyle(Paint.Style.STROKE);
        cur_paint.setStrokeWidth(5);

        if(type==Type.head){
            rectf = new RectF(-60,-170,60,0);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.uparm){
            rectf = new RectF(-30,0,30,200);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.downarm){
            rectf = new RectF(-25,0,25,160);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.hand){
            rectf = new RectF(-25,0,25,50);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.torso){
            rectf = new RectF(0,0,200,320);
            canvas.drawRoundRect(rectf,50,50,cur_paint);
        } else if(type==Type.upleg){
            rectf = new RectF(-25,0,25,220);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.downleg){
            rectf = new RectF(-22,0,22,250);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.leftfeet){
            rectf = new RectF(-90,0,0,50);
            canvas.drawOval(rectf,cur_paint);
        } else if(type==Type.rightfeet){
            rectf = new RectF(0,0,90,50);
            canvas.drawOval(rectf,cur_paint);
        }

        for(Body child:children){
            child.draw(canvas);
            canvas.restore();
        }


    }










}
