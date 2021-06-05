import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.Timer;

public class GamingPage extends JPanel {
    final int block_interval = 110; //100 -> 110
    final int block_number = (1000 / block_interval + 3) * 4;
    final int dead_line_y = 1000 + block_interval;
    int current_load = block_number / 4;
    ArrayList<Integer> trail_number = new ArrayList<Integer>();
    Stuff objects[] = new Stuff[block_number];
    String str[] = new String[100];
    boolean drawblock[] = new boolean[block_number];
    int index = 0;
    int counter = 0;
    int status = 0;
    double fall_speed = 1;
    Stroke stroke1 = new BasicStroke(6f);
    int upper_upper_str;
    public int upper_str_ypos = 0;
    public String upper_str = "1000";
    int temp = 0;
    int cur = 1;
    //以下是元
    JLabel back = new JLabel();
    JLabel Mogu_pos = new JLabel(new ImageIcon("src/img/MOGU.png"));
    JLabel Score_img = new JLabel();
    JLabel rec = new JLabel();
    JLabel MyScore = new JLabel();
    JLabel EnemyScore = new JLabel();
    JLabel Combo = new JLabel();
    JLabel MyScore_f = new JLabel();
    JLabel EnemyScore_f = new JLabel();
    JLabel Combo_f = new JLabel();
    public Integer my_score = 1; //自己分數 
    public Integer enemy_score = 1; //敵人分數 
    public Integer combo = 1; //Combo數
    public String perfect = "perfect"; //choose effect
    public Integer mushroom = 1000; //蘑菇頭位置

    GamingPage() {

        setLayout(null);
        back = new JLabel(new ImageIcon("src/img/bg_GamingPage.png"));
        back.setOpaque(true);
        back.setBounds(0, 0, 1600, 1000);
        back.setBackground(new Color(0x123456));
        for (int i = 0; i < 4; i++) {
            rec = new JLabel(new ImageIcon("src/img/rect.png"));
            rec.setBounds(590 + 200 * i, 865, 200, 50);
            add(rec);
        }

        setMOGU(mushroom);
        add(Mogu_pos);

        displayScoreImg(perfect);
        add(Score_img);

        PaintScore(my_score, enemy_score, combo);
        add(MyScore);
        add(EnemyScore);
        add(Combo);
        add(MyScore_f);
        add(EnemyScore_f);
        add(Combo_f);

        add(back);

        readfile(); //read sheet.txt to str array
        initial();
        check_if_buttom();
    }
    // public void check()
    // {
    //     String temp;
    //     Timer t3 = new Timer();
    //     TimerTask check_y = new TimerTask(){
    //         public void run() {

    //         }
    //     };
    //     t3.schedule(check_y, 0,5);
    //}

    public void PaintScore(Integer my, Integer en, Integer com) {
        Timer s = new Timer();
        TimerTask ss = new TimerTask() {
            @Override
            public void run() {

                my_score += my;
                MyScore.setText(my_score.toString());
                MyScore.setForeground(Color.CYAN);
                MyScore.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 100));
                MyScore.setBounds(260, 170, 500, 200);

                enemy_score += en;
                EnemyScore.setText(enemy_score.toString());
                EnemyScore.setForeground(Color.RED);
                EnemyScore.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 100));
                EnemyScore.setBounds(260, 70, 500, 200);

                combo += com;
                Combo.setText(combo.toString());
                Combo.setForeground(new Color(0x154354)); //0x587587
                Combo.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 100));
                Combo.setBounds(300, 370, 500, 200);

                MyScore_f.setText("Score _____");
                MyScore_f.setForeground(Color.CYAN); //0x587587
                MyScore_f.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 80));
                MyScore_f.setBounds(100, 200, 500, 200);

                EnemyScore_f.setText("Score _____");
                EnemyScore_f.setForeground(Color.RED); //0x587587
                EnemyScore_f.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 80));
                EnemyScore_f.setBounds(100, 100, 500, 200);

                Combo_f.setText("Combo ___");
                Combo_f.setForeground(new Color(0x154354)); //0x587587
                Combo_f.setFont(new Font("Playlist", Font.ROMAN_BASELINE, 80));
                Combo_f.setBounds(100, 400, 500, 200);
            }
        };
        s.schedule(ss, 0, 1);
    }

    public void displayScoreImg(String perfect) {
        Timer t6 = new Timer();
        TimerTask check_score = new TimerTask() {

            public void run() {
                switch (perfect) {
                    case "perfect":
                        ImageIcon p = new ImageIcon("src/img/perfect.png");
                        Score_img.setIcon(p);
                        break;
                    case "good":
                        ImageIcon g = new ImageIcon("src/img/good.png");
                        Score_img.setIcon(g);
                        break;
                    case "bad":
                        ImageIcon b = new ImageIcon("src/img/bad.png");
                        Score_img.setIcon(b);
                        break;
                }
                Score_img.setBounds(300, 700, 300, 300);
            }
        };
        t6.schedule(check_score, 0, 1);
    }

    public void setMOGU(Integer mush) {
        /*
            第一軌道: 635,830
            第二軌道: 835,830
            第三軌道: 1035,830
            第四軌道: 1235,830
            595 ~ 1325, 830
        */
        Timer t5 = new Timer();
        TimerTask check_MOGU = new TimerTask() {
            public void run() {
                Mogu_pos.setBounds(mush, 830, 100, 100);
            }
        };
        t5.schedule(check_MOGU, 0, 1);

    }

    public void initial() {
        for (int i = 0; i < block_number; i++) {
            objects[i] = new Stuff();
            objects[i].ypos = -200;
            drawblock[i] = true;
        }
        double time = 0;
        for (int line = 0; line < block_number / 4; line++) {
            for (int at = 0; at < 4; at++) {

                if (str[line].charAt(at) == '1') {
                    objects[at + 4 * line].set_value(620 + (200 * at), time, fall_speed);
                } else {
                    objects[at + 4 * line].set_value(-200, time, fall_speed);
                }
                block(objects[at + 4 * line]);
            }
            time -= block_interval;
        }
    }

    public void readfile() {
        try {
            FileReader fr = new FileReader("beatmap/YEEYEEYEEYEEYEE.txt");
            BufferedReader br = new BufferedReader(fr);
            while (true) {
                str[counter] = br.readLine();
                if (str[counter] == null)
                    break;
                counter++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fail read file!!", "Default", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void paint(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Graphics2D g2D = (Graphics2D) g;
        super.paint(g);

        for (int i = 0; i < block_number; i++) {
            if (drawblock[i])
                objects[i].paintBlock(g);
        }
        paintline(g);
    }

    public void block(Stuff object) {
        Timer t2 = new Timer();
        TimerTask ttt = new TimerTask() {
            public void run() {
                object.ypos = object.ypos + object.speed;
                repaint(); // 重繪panel
            }
        };
        t2.schedule(ttt, 0, 5);
    }

    public void paintline(Graphics g) {
        Stroke stroke2 = new BasicStroke(20f);
        Graphics2D g2d_1 = (Graphics2D) g;
        g2d_1.setColor(Color.ORANGE);
        g2d_1.setStroke(stroke2);
        for (int i = 0; i < index - 1; i++) {
            g2d_1.drawLine((int) objects[trail_number.get(i)].xpos, (int) objects[trail_number.get(i)].ypos,
                    (int) objects[trail_number.get(i + 1)].xpos, (int) objects[trail_number.get(i + 1)].ypos);
        }
    }

    public void check_if_buttom() {
        Timer t3 = new Timer();
        TimerTask check_y = new TimerTask() {
            public void run() {
                upper_str_ypos = 919 - (int) objects[temp].ypos;
                if (objects[temp].ypos == 919) {
                    upper_str = str[cur];
                    temp += 4;
                    cur++;
                }
                if (temp >= block_number)
                    temp = 0;
                if (index > 0) {
                    if (objects[trail_number.get(0)].ypos >= dead_line_y) {
                        drawblock[trail_number.get(0)] = true;
                        trail_number.remove(0);
                        index--;
                    }
                }
                if (current_load < counter) {
                    for (int i = 0; i < counter; i++) {
                        int line = i % (block_number / 4); //第i列資料
                        if (objects[line * 4].ypos >= dead_line_y) {
                            for (int track = 0; track < 4; track++) {
                                if (str[current_load].charAt(track) == '2') {
                                    if (index >= (block_number / 4)) {
                                        trail_number.remove(0);
                                        index--;
                                    }
                                    trail_number.add(track + 4 * line);
                                    objects[track + 4 * line].set_value(660 + 200 * track, -180, fall_speed);
                                    index++;
                                    drawblock[track + 4 * line] = false;
                                } else if (str[current_load].charAt(track) == '1') {
                                    objects[track + 4 * line].set_value(620 + 200 * track, -200, fall_speed);
                                } else {
                                    objects[track + 4 * line].set_value(-200, -200, fall_speed);
                                }
                            }
                            current_load++;
                        }
                    }
                    repaint();
                }
            }
        };
        t3.schedule(check_y, 0, 1);
    }
}