package com.grocafast.grocafast;

import static com.grocafast.grocafast.GetAllPermutations.getPermutations;
import static com.grocafast.grocafast.LoginActivity.shopping_items;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.grocafast.grocafast.databinding.ActivityShortestPathBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.grocafast.grocafast.LoginActivity.usernameText;

public class ShortestPath extends AppCompatActivity {

    DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Users").child(usernameText);

    Button finish;
    ImageView imageView;

    Bitmap bitmap;
    TextView textView;
    private ImageView back;

    private ActivityShortestPathBinding binding;

    Mat ground_pixels,ground_pixels_tree_mark,bigger;

    // colors definishions
    double[] white = new double[3] ;
    double[] green = new double[3] ;
    double[] yellow = new double[3] ;
    double[] orange = new double[3] ;
    double[] lightgreen = new double[3] ;

    // map squares width and height
    int cols = 23;
    int rows = 11;
    int[][] paths_all;
    int shortest_path_number_all = 0;
    // assigning entrance point
    Point entrance = new Point(6,0);
    Point exit = new Point(8,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_shortest_path);


        back = findViewById(R.id.back);

        //on click button action call open home page method
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        DatabaseReference user_lists_reference = user_reference.child("lists");

        user_lists_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        Date date = new Date();
                        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String stringdate = dt.format(newDate);
                        String key = user_reference.child("lists").push().getKey();
                        user_reference.child("lists").child(String.valueOf(key)).child("date").setValue(stringdate);
                        for(int i = 0; i < shopping_items.length; i++){
                            user_reference.child("lists").child(String.valueOf(key)).child("items").child(String.valueOf(i)).setValue(shopping_items[i].itemName);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//        user_reference.child("lists").child("1").setValue("asd");

        if(OpenCVLoader.initDebug()){
        }

        Thread thread = new Thread(runnable);
        thread.start();

        imageView = findViewById(R.id.imageView);

        // initializing opencv

        Toast.makeText(getApplicationContext(),"Processing Please wait", Toast.LENGTH_LONG).show();
    }

    Runnable runnable = new Runnable(){
        public void run() {
            // defining colors // any color that is a barrier should have [0] = 255
            white[0] = 255;white[1] = 255;white[2] = 255;
            green[0] = 0;green[1] = 200;green[2] = 0;
            yellow[0] = 0;yellow[1] = 200;yellow[2] = 200;
            orange[0] = 20;orange[1] = 170;orange[2] = 200;
            lightgreen[0] = 180;lightgreen[1] = 255;lightgreen[2] = 176;
// creating map and drawing map
            ground_pixels = Mat.zeros(rows,cols, CvType.CV_8UC3);
            ground_pixels.put(0,0, white);
            ground_pixels.put(1,0, white);
            ground_pixels.put(2,0, white);
            ground_pixels.put(3,0, white);
            ground_pixels.put(4,0, white);
            ground_pixels.put(5,0, white);
            ground_pixels.put(6,0, white);
            ground_pixels.put(7,0, white);
            ground_pixels.put(8,0, white);
            ground_pixels.put(9,0, white);
            ground_pixels.put(10,0, white);

            ground_pixels.put(0,1, white);
            ground_pixels.put(0,2, white);
            ground_pixels.put(0,3, white);

            ground_pixels.put(10,1, white);
            ground_pixels.put(10,2, white);
            ground_pixels.put(10,3, white);
            ground_pixels.put(10,4, white);

            ground_pixels.put(4,3, white);
            ground_pixels.put(5,3, white);
            ground_pixels.put(6,3, white);
            ground_pixels.put(7,3, white);
            ground_pixels.put(4,4, white);
            ground_pixels.put(5,4, white);
            ground_pixels.put(6,4, white);
            ground_pixels.put(7,4, white);

            ground_pixels.put(3,7, white);
            ground_pixels.put(4,7, white);
            ground_pixels.put(5,7, white);
            ground_pixels.put(6,7, white);
            ground_pixels.put(7,7, white);
            ground_pixels.put(3,8, white);
            ground_pixels.put(4,8, white);
            ground_pixels.put(5,8, white);
            ground_pixels.put(6,8, white);
            ground_pixels.put(7,8, white);

            ground_pixels.put(4,11, white);
            ground_pixels.put(5,11, white);
            ground_pixels.put(6,11, white);
            ground_pixels.put(7,11, white);
            ground_pixels.put(8,11, white);
            ground_pixels.put(4,12, white);
            ground_pixels.put(5,12, white);
            ground_pixels.put(6,12, white);
            ground_pixels.put(7,12, white);
            ground_pixels.put(8,12, white);

            ground_pixels.put(3,15, white);
            ground_pixels.put(4,15, white);
            ground_pixels.put(5,15, white);
            ground_pixels.put(6,15, white);
            ground_pixels.put(7,15, white);
            ground_pixels.put(3,16, white);
            ground_pixels.put(4,16, white);
            ground_pixels.put(5,16, white);
            ground_pixels.put(6,16, white);
            ground_pixels.put(7,16, white);

            ground_pixels.put(10,18, white);
            ground_pixels.put(10,19, white);

            ground_pixels.put(3,19, white);
            ground_pixels.put(4,19, white);
            ground_pixels.put(5,19, white);
            ground_pixels.put(6,19, white);
            ground_pixels.put(7,19, white);
            ground_pixels.put(3,20, white);
            ground_pixels.put(4,20, white);
            ground_pixels.put(5,20, white);
            ground_pixels.put(6,20, white);
            ground_pixels.put(7,20, white);

            ground_pixels.put(9,22, white);
            ground_pixels.put(10,22, white);

            // factorial is all possible combinations routes count between items
            int factorial = 1;  // 3 items , 3x2x1  ,  6 routes
            for(int i = 1; i <= shopping_items.length; i++){
                factorial = factorial * i;
            }

            // all possible routes between items
            int[] numbersItems = new int [shopping_items.length]; // 0 1 2
            for (int i = 0; i< shopping_items.length; i++){
                numbersItems[i]=i;
            }


            // all possible combinations between items
            paths_all = getPermutations(numbersItems,factorial);
            // [0 1 2]
            // [0 2 1]
            // [1 0 2]
            // [1 2 0]
            // [2 0 1]
            // [2 1 0]


            // printing paths_all
            for (int i=0;i<factorial;i++){  // 6
                System.out.print("[");
                for (int j = 0; j< shopping_items.length; j++){
                    System.out.print( paths_all[i][j] + ", ");
                }
                System.out.println("]");
            }

            // storing the distance between each pair for optimization
            double[][] distances_matrix = new double[shopping_items.length+2][shopping_items.length+2];
            for (int x=0; x < shopping_items.length ; x++) {
                distances_matrix[x+1][0] = get_shortest_path_length(new Point( entrance.y , entrance.x ) , new Point(shopping_items[x].y, shopping_items[x].x));
                distances_matrix[x+1][shopping_items.length+1] = get_shortest_path_length(new Point( exit.y , exit.x ) , new Point(shopping_items[x].y, shopping_items[x].x));
            }
            for (int y=0; y < shopping_items.length ; y++) {
                distances_matrix[0][y+1] = get_shortest_path_length(new Point( entrance.y , entrance.x ) , new Point(shopping_items[y].y, shopping_items[y].x));
                distances_matrix[shopping_items.length+1][y+1] = get_shortest_path_length(new Point( exit.y , exit.x ) , new Point(shopping_items[y].y, shopping_items[y].x));
            }

            for (int x=0; x < shopping_items.length ; x++) {
                for (int y = 0; y < shopping_items.length ; y++) {
                    if (x != y) {
                        distances_matrix[x+1][y+1] = get_shortest_path_length(new Point(shopping_items[x].y, shopping_items[x].x), new Point(shopping_items[y].y, shopping_items[y].x));
                    }
                }
            }

            // determining shortest path - all items
            shortest_path_number_all = 0;
            double shortest_path_length_all = 99999999;
            for (int i=0;i<factorial;i++){
                double pathlength = 0;
//            pathlength += get_shortest_path_length(new Point( entrance.y , entrance.x ) , new Point( (shopping_items[paths_all[i][0]].gety()) ,shopping_items[paths_all[i][0]].getx() ) );
                pathlength += distances_matrix[0][paths_all[i][0]+1];
                for (int j = 0; j< shopping_items.length-1; j++){
//                pathlength += get_shortest_path_length(new Point(shopping_items[paths_all[i][j]].y , shopping_items[paths_all[i][j]].x ) , new Point(shopping_items[paths_all[i][j+1]].y ,shopping_items[paths_all[i][j+1]].x ) );
                    pathlength += distances_matrix[paths_all[i][j]+1][paths_all[i][j+1]+1];
                }
                pathlength += distances_matrix[paths_all[i][shopping_items.length-1]][shopping_items.length+1];
                if ( pathlength < shortest_path_length_all){
                    shortest_path_length_all = pathlength ;
                    shortest_path_number_all = i;
                }
            }

            Draw_delayed_path();
        }
    };

    int draw_this_path;
    int current_count = 0, previous_count = 0;boolean finished_draw_path = false,finished_path_one=false,finished_paths_mid=false,finished_path_fin=false;

    void Draw_delayed_path(){
        // draw shortest path - all items
        if (!finished_path_one) {
            get_shortest_path_length(new Point(entrance.y, entrance.x), new Point(shopping_items[paths_all[shortest_path_number_all][0]].y, shopping_items[paths_all[shortest_path_number_all][0]].x));
            draw_this_path = shortest_path;
            for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                if (current_count < previous_count) {
                    ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, lightgreen);
                    current_count++;
                }
                if (current_count == previous_count) {
                    current_count = 0;
                    previous_count++;
                    break;
                }
            }
            if (previous_count == each_path_lengths[draw_this_path]){
                finished_path_one = true;
                current_count = 0 ;
                previous_count = 0;
            }
        }else{
            get_shortest_path_length(new Point(entrance.y, entrance.x), new Point(shopping_items[paths_all[shortest_path_number_all][0]].y, shopping_items[paths_all[shortest_path_number_all][0]].x));
            draw_this_path = shortest_path;
            for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, lightgreen);
            }
        }


        boolean dont_do_next = false;
        outerloop:
        if (finished_path_one) {
            if (!finished_paths_mid) {
                if (!dont_do_next) {
                    for (int i = 0; i < shopping_items.length - 1; i++) {
                        get_shortest_path_length(new Point(shopping_items[paths_all[shortest_path_number_all][i]].y, shopping_items[paths_all[shortest_path_number_all][i]].x), new Point(shopping_items[paths_all[shortest_path_number_all][i + 1]].y, shopping_items[paths_all[shortest_path_number_all][i + 1]].x));
                        draw_this_path = shortest_path;
                        for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                            if (current_count < previous_count) {
                                ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, green);
                                current_count++;
                            }
                            if (current_count == previous_count) {
                                current_count = 0;
                                previous_count++;
                                dont_do_next = true;
                                break outerloop;
                            }

                        }
                    }
                    finished_paths_mid = true;
                    current_count = 0 ;
                    previous_count = 0;
                }
            } else {
                for (int i = 0; i < shopping_items.length - 1; i++) {
                    get_shortest_path_length(new Point(shopping_items[paths_all[shortest_path_number_all][i]].y, shopping_items[paths_all[shortest_path_number_all][i]].x), new Point(shopping_items[paths_all[shortest_path_number_all][i + 1]].y, shopping_items[paths_all[shortest_path_number_all][i + 1]].x));
                    draw_this_path = shortest_path;
                    for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                        ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, green);

                    }
                }
            }
        }

        if (finished_paths_mid ) {
            if (!finished_path_fin) {
                get_shortest_path_length(new Point(shopping_items[paths_all[shortest_path_number_all][shopping_items.length - 1]].y, shopping_items[paths_all[shortest_path_number_all][shopping_items.length - 1]].x), new Point(exit.y, exit.x));
                draw_this_path = shortest_path;
                for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                    if (current_count < previous_count) {
                        ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, orange);
                        current_count++;
                    }
                    if (current_count == previous_count) {
                        current_count = 0;
                        previous_count++;
                        break;
                    }
                }
                if (previous_count == each_path_lengths[draw_this_path]) {
                    finished_path_fin = true;
                    current_count = 0;
                    previous_count = 0;
                }
            } else {
                get_shortest_path_length(new Point(shopping_items[paths_all[shortest_path_number_all][shopping_items.length - 1]].y, shopping_items[paths_all[shortest_path_number_all][shopping_items.length - 1]].x), new Point(exit.y, exit.x));
                draw_this_path = shortest_path;
                for (int nn = 0; nn < each_path_lengths[draw_this_path] - 1; nn++) {
                    ground_pixels.put((int) paths[draw_this_path][nn].x, (int) paths[draw_this_path][nn].y, orange);
                }
            }
        }

        Send_ground_pixels_to_view();

        if (!finished_path_fin) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Draw_delayed_path();
                }
            }, 10);
        }
    }

    void Send_ground_pixels_to_view(){


        // drawing items with yellow
        for (int i=0;i<shopping_items.length;i++){
            ground_pixels.put(shopping_items[i].y,shopping_items[i].x, yellow);
        }
        // drawing entrance
        ground_pixels.put((int) entrance.y, (int) entrance.x, yellow);
        ground_pixels.put((int) exit.y, (int) exit.x, yellow);
// creating scaled image
        int scale = 20; // scale
        int box_d = 18; // box width
        bigger = Mat.zeros(rows * scale,cols * scale, CvType.CV_8UC3); // scaled map
        for (int x=0 ; x< ground_pixels.rows() ;x++)
            for (int y=0 ; y< ground_pixels.cols() ;y++)
                Imgproc.rectangle(bigger, new Point(y *scale + scale/2-box_d/2, x *scale+ scale/2-box_d/2), new Point(y *scale + scale/2+box_d/2, x *scale+ scale/2+box_d/2), new Scalar(ground_pixels.get(x,y)[0], ground_pixels.get(x,y)[1], ground_pixels.get(x,y)[2]), -1);

        // writing points order on image
        for (int i=0;i<shopping_items.length;i++){
            Imgproc.putText(bigger, shopping_items[paths_all[shortest_path_number_all][i]].itemName , new Point(shopping_items[paths_all[shortest_path_number_all][i]].x*scale,shopping_items[paths_all[shortest_path_number_all][i]].y*scale) , Imgproc.FONT_HERSHEY_SIMPLEX, .5, new Scalar(0, 0, 255), 1);
        }

        // Opencv Mat BRG to android Mat RGB
        Bitmap bmp = null;
        Mat rgb = new Mat();//[  0 ,255 , 255 ....... ]
        Imgproc.cvtColor(bigger, rgb, Imgproc.COLOR_BGR2RGBA);

        try {
            // android Mat -> Bitmap
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
//            imageView.setImageBitmap(bmp);
            Bitmap finalBmp = bmp;
            ShortestPath.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(finalBmp);
                }
            });
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
    }

    // return the surrounding black points of a point in an Mat(image)
    // used to determine next step in path
    int black_surrounding_count = 0;  // black points found count
    Point[] black_surrounding = new Point[8]; // initializing black points empty array of max size 8
    void get_surrounding_black(Mat ground_pixels,Point point){
        black_surrounding_count = 0;
        // looping on points surrounding the point
        for (int c = (int)point.x -1  ; c< (int)point.x+2 ;c++) // looping on x axis
            for (int r=(int)point.y-1 ; r< (int)point.y+2 ;r++) // looping on y axis
                // checking if point is inside map boundaries and point is not the original point
                if ( r >= 0 & c >= 0 & r < ground_pixels.cols() & c < ground_pixels.rows() & !(c == point.x & r== point.y) & (c == point.x | r== point.y))
                    if (ground_pixels.get(c,r)[0] != 255) { // checking if point is black
                        black_surrounding[black_surrounding_count] = new Point(c,r); // storing black point
                        black_surrounding_count++; // adding 1 to black points count
                    }
    }

    // check if a point exists in a set of points
    // used to check if destination is the black points - to mark the end of the
    boolean is_point_in_points(Point point,Point[] points){
        boolean point_in_points = false;
        for (int n=0;n<black_surrounding_count;n++){ // looping on all points
            if (point.x == points[n].x & point.y == points[n].y){
                return true;
            }
        }
        return point_in_points;
    }

    // stores all possible paths between 2 points on the map
    int paths_count = 0;  // paths count
    int max_paths_count = 210; //  max paths count
    int max_point_per_path = 200; // maximum number of points per path
    int[] each_path_lengths = new int[max_paths_count];  // initializing paths lengths array   [ 17  10  20 15]
    Point[][] paths = new Point[max_paths_count][max_point_per_path]; // initializing paths array
    //    [(p11) (p12) (p13) (p14) (p15)]
//    [(p21) (p22) (p23) (p24)]
//    [(p31) (p32) (p33) (p34) (p35) (p36)]
    void find_paths_tree(Mat ground_pixels,Point point_src,Point point_dst){
        ground_pixels_tree_mark = new Mat();  // creating a copy of the map to mark the paths
        ground_pixels.copyTo(ground_pixels_tree_mark);
        ground_pixels_tree_mark.put((int) point_src.x, (int) point_src.y, white); // marking source point on map
        paths[0][0] = point_src; paths_count++; each_path_lengths[0]++; // resting first path with first point
        int loop_count=0; // maximum points in paths
        int max_loops = 100; // should be 40 - 100 or more
        while (loop_count < max_loops){ // keep walking on paths
            int paths_count_tmp = paths_count;
            // looping partial paths to continue
            for (int p=0;p<paths_count_tmp;p++){ // continuing roads.
                // check if this route didn't reach the destination yet
                if (!(paths[p][each_path_lengths[p]-1].x == point_dst.x & paths[p][each_path_lengths[p]-1].y == point_dst.y)){
                    // get blacks to follow
                    get_surrounding_black(ground_pixels_tree_mark,paths[p][each_path_lengths[p]-1]);
                    // if one of the surroundings is the destination.
                    if ( is_point_in_points(point_dst,black_surrounding) ){
                        paths[p][each_path_lengths[p]] = point_dst; // add the destination to be the final point
                        each_path_lengths[p]++;
                    }else { // not reached
                        for (int n=0;n < black_surrounding_count;n++){
                            if (n==0){ // add first black point to the end of courent route
                                paths[p][each_path_lengths[p]] = new Point(black_surrounding[n].x,black_surrounding[n].y);
                                each_path_lengths[p]++;
                                ground_pixels_tree_mark.put((int) black_surrounding[n].x, (int) black_surrounding[n].y, white);
                            }else{ // create (branches) clones of the route with the other black points
                                for (int nn=0;nn<each_path_lengths[p]-1;nn++){
                                    paths[paths_count][nn] =  paths[p][nn];
                                }
                                each_path_lengths[paths_count] = each_path_lengths[p];
                                paths[paths_count][each_path_lengths[paths_count]-1] = new Point(black_surrounding[n].x,black_surrounding[n].y);
                                ground_pixels_tree_mark.put((int) black_surrounding[n].x, (int) black_surrounding[n].y, white);
                                paths_count++;
                            }
                        }
                    }
                }
            }
            loop_count++;
        }
    }

    // obtain the shortest path between 2 points from the paths(tree)
    int shortest_path;
    double get_shortest_path_length(Point point_src,Point point_dst){
        paths_count = 0; // resetting paths count
        each_path_lengths = new int[max_paths_count]; // resetting paths lengths
        paths = new Point[max_paths_count][max_point_per_path]; // resetting paths
        find_paths_tree(ground_pixels,point_src,point_dst);  // acquiring all possible paths tree

        // selecting shortest path from paths
        shortest_path = 0;
        int shortest_path_length = 99999;
        // selecting shortest path
        for (int p=0;p<paths_count-1;p++){  // looping on all paths
            if (paths[p][each_path_lengths[p]-1].x == point_dst.x & paths[p][each_path_lengths[p]-1].y == point_dst.y){
                if (each_path_lengths[p] < shortest_path_length){ // if path < paths , mark path as shortest
                    shortest_path_length = each_path_lengths[p];
                    shortest_path = p;
                }
            }
        }
        return shortest_path_length;
    }

    //open home page
    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}