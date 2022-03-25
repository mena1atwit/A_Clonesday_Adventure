package com.game.game1;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Timer;
import java.util.TimerTask;


public class BinaryTree  {

    public int rand;
    public Node root;

    public void aBinaryTree() {
        Node a = new Node((7));
        Node b = new Node((5));
        Node c = new Node((9));
        Node d = new Node((10));
        Node e = new Node((3));

        setRoot(a);
        addLeft(a, b);
        addRight(a, c);
        addRight(c, d);
        addLeft(b,e);

    }
    public void setRoot(Node a){
        root = a;
    }
    public void addRight(Node a, Node b){
        a.right = b;
    }
    public void addLeft(Node a, Node b){
        a.left = b;
    }
    public int getLast(BinaryTree tree){
        return 0;

    }

    public static void getPreorder(Node root) {
        if (root == null) {

            return;
        }
        System.out.print(root.num + " ");
        getPreorder(root.left);
        getPreorder(root.right);
    }

    public static void getPostorder(Node root) {
        if (root == null) {
            return;
        }
        getPostorder(root.left);
        getPostorder(root.right);
        System.out.print(root.num + " ");

    }

    public static void getInorder(Node root) {
        if (root == null) {
            return;
        }
        getInorder(root.left);
        System.out.print(root.num + " ");
        getInorder(root.right);

    }

    public int random(BinaryTree binary) {

        int rand = (int) (Math.random() * 3);

        if (rand == 0) {
            getPreorder(binary.root);

        }
        if (rand == 1) {
            getPostorder(binary.root);

        }
        if (rand == 2) {
            getInorder(binary.root);

        }
        return rand;

    }

    class Time extends TimerTask {
        int sec=0;
        int repeat = 0;
        @Override
        public void run() {
            sec++;
            BinaryTree bt = new BinaryTree();
            bt.aBinaryTree();
            // System.out.println("seconds: " + sec);

            if (sec == 10) {
                sec = 0;
                int a = random(bt);
                repeat++;
                System.out.println(a);
            }
            if(repeat == 6){
                System.exit(0);
            }
        }
    };

    public void time() {

        Timer time = new Timer();
        Time times = new Time();

        time.schedule(times, 1000, 1000);


    }


}





