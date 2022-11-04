import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class MineSweeper extends JFrame implements ActionListener{

    final int easy[] = {8, 50, 10};   // 難易度別{1列のセルの数、セル一辺の長さ、爆弾の数}
    final int normal[] = {16, 30, 40};
    final int hard[] = {24, 20, 100};

    int btn;    // セル番号
    int key;    // 押されたキー情報
    int i, j, k;   // カウンタ変数
    int level[] = new int[3];
    String bomsAmount;    // 周りの爆弾の数を文字にして代入する変数
    String cmd; // ActionCommandを格納する変数
    JPanel panel = new JPanel();    // パネルを利用;
    ArrayList<Integer> list = new ArrayList<Integer>(); // 爆弾を配置するために必要

    int cell;   // 1列のセルの数
    int cells;  // ボード全てのセルの数
    int side;    // セル一辺の長さ
    int boms; // 爆弾の数
    int bom[];  // 爆弾設置のセル番号
    int num[];    // 周りの爆弾数
    int victoryNum;
    JButton button[]; // ボードの生成
    
    // 開発用に作成（実際のプレーではインスタンス化して使用するのでここから起動しない）
    // public static void main(String[] args) {
    //     int inputNum = 1;
    //     MineSweeper frame = new MineSweeper("MineSweeper", inputNum);
    //     frame.setVisible(true);
    // }

    MineSweeper(String title, int inputNum){
        switch(inputNum){
            case 1:
                level = easy;
                break;
            case 2:
                level = normal;
                break;
            case 3:
                level = hard;
                break;
        }
        
        // レベル別に設定できるように作り直し
        cell = level[0];               // 1列のセルの数
        cells = cell * cell;                 // ボード全てのセルの数
        side = level[1];               // セル一辺の長さ
        boms = level[2];            // 爆弾の数
        bom = new int[boms];        // 爆弾設置のセル番号
        num = new int[cells];          // 周りの爆弾数
        victoryNum = cells - boms;
        button = new JButton[cells];   // ボードの生成
    
        setTitle(title);
        // setLocationRelativeTo(null);
        setSize(cell * side + 10, cell * side + 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setButtons();
        setBoms();
        setNumber();

        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    void setButtons(){
        panel.setLayout(null);

        for(i=0; i<cell; i++){
            for(j=0;j<cell;j++){
                btn = i*cell+j;    // 0〜exまでの番号
                button[btn] = new JButton();
                button[btn].setOpaque(true);    // セルの透明性の有効
                // button[btn].setBorderPainted(false);  // セルの枠線を消す
                button[btn].setBackground(Color.LIGHT_GRAY);
                button[btn].setBounds(i*side, j*side, side, side);
                button[btn].addActionListener(this);
                button[btn].setActionCommand("a" + btn);

                panel.add(button[btn]);
            }
        }
    }

    void setBoms(){ 
        // listに値を入れる。この段階では昇順
        for(int i = 0 ; i <= cells ; i++) {
            list.add(i);
        }

        Collections.shuffle(list);  // ArrayListをシャッフル
        
        for(i=0; i<boms; i++){
            bom[i] = list.get(i);   // ArrayListから爆弾セル番号を格納
            // 開発時に爆弾を意図的に表示する場合は下の行をコメントアウト解除する
            // button[bom[i]].setText("*");
        }
    }

    public void setNumber() {
        out:    
            for(i=0;i<boms;i++){
                num[bom[i]] = 10;
                if(bom[i] == 0){    // 左上
                    num[bom[i] + 1]++;
                    num[bom[i] + cell]++;
                    num[bom[i] + cell + 1]++;
                }else if(bom[i] == cell - 1){  // 右上
                    num[bom[i] - 1]++;
                    num[bom[i] + cell]++;
                    num[bom[i] + cell - 1]++;
                }else if(bom[i] == cells - cell){ // 左下
                    num[bom[i] - cell]++;
                    num[bom[i] - cell + 1]++;
                    num[bom[i] + 1]++;
                }else if(bom[i] == cells - 1){ // 右下
                    num[bom[i] - 1]++;
                    num[bom[i] - cell]++;
                    num[bom[i] - cell - 1]++;
                }else if(bom[i] < cell){   // 1番上列
                    num[bom[i] - 1]++;
                    num[bom[i] + 1]++;
                    num[bom[i] + cell - 1]++;
                    num[bom[i] + cell]++;
                    num[bom[i] + cell + 1]++;
                }else if(bom[i] < cells && bom[i] > cells - cell){   // 1番下の列
                    num[bom[i] - cell - 1]++;
                    num[bom[i] - cell]++;
                    num[bom[i] - cell + 1]++;
                    num[bom[i] - 1]++;
                    num[bom[i] + 1]++;
                }else{
                    for(j=0;j<cell;j++){
                        k = j * cell;
                        if(bom[i] == k){    // 1番左列
                            num[bom[i] - cell]++;
                            num[bom[i] - cell + 1]++;
                            num[bom[i] + 1]++;
                            num[bom[i] + cell]++;
                            num[bom[i] + cell + 1]++;

                            continue out;   // 以降の処理を飛ばす
                        }
                    }

                    for(j=0;j<cell;j++){
                        k = j * cell - 1;
                        if(bom[i] == k){    // 1番右列
                            num[bom[i] - cell - 1]++;
                            num[bom[i] - cell]++;
                            num[bom[i] - 1]++;
                            num[bom[i] + cell - 1]++;
                            num[bom[i] + cell]++;

                            continue out;   // 以降の処理を飛ばす
                        }
                    }

                    num[bom[i] - cell - 1]++;
                    num[bom[i] - cell]++;
                    num[bom[i] - cell + 1]++;
                    num[bom[i] - 1]++;
                    num[bom[i] + 1]++;
                    num[bom[i] + cell - 1]++;
                    num[bom[i] + cell]++;
                    num[bom[i] + cell + 1]++;
                }
            }
    }

    public boolean openCells(int cellNum){
        // System.out.println(cellNum);    // デバッグ用
        if(cellNum < 0 || cellNum > cells){
            return false;
        }
        if(button[cellNum].isEnabled()){
            if(num[cellNum]<10){      // 爆弾セルの番号を10にしているので10以下であればセーフ
                bomsAmount = "" + num[cellNum];
                button[cellNum].setEnabled(false);
                button[cellNum].setBackground(Color.GRAY);
                victoryNum--;
                if(victoryNum == 0){ // 勝利条件
                    new Result("result", 1);
                }
                if(num[cellNum] != 0){
                    button[cellNum].setText(bomsAmount);
                    return false;
                }else{
                    if(cellNum - cell >= 0){
                        openCells(cellNum - cell);
                    }
                    if(cellNum % cell != 0){
                        openCells(cellNum - cell - 1);
                        openCells(cellNum - 1);
                        openCells(cellNum + cell - 1);
                    }
                    if((cellNum + 1) % cell != 0){
                        openCells(cellNum - cell + 1);
                        openCells(cellNum + 1);
                        openCells(cellNum + cell + 1);
                    }
                    if(cellNum + cell <= cells){
                        openCells(cellNum + cell);
                    }
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
    }

    public void checkBom(int clickPlace) {
        for(int bomInspection : bom){
            if(bomInspection == clickPlace){
                new Result("result", 2);
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        cmd = e.getActionCommand();
        key = e.getModifiers();
        checkLoop:
        for(i=0; i<cells; i++){
            if(cmd.equals("a" + i)){
                if((key & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK){
                    button[i].setForeground(Color.RED);
                    button[i].setText("×");
                    button[i].setActionCommand("b" + i);
                }else{
                    while(openCells(i));
                    checkBom(Integer.parseInt(cmd.substring(1)));
                    break checkLoop;
                }                
            }

            if(cmd.equals("b" + i)){
                if((key & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK){
                    button[i].setForeground(Color.BLUE);
                    button[i].setText("？");
                    button[i].setActionCommand("c" + i);
                }
            }

            if(cmd.equals("c" + i)){
                if((key & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK){
                    button[i].setBackground(Color.LIGHT_GRAY);
                    button[i].setText("");
                    button[i].setActionCommand("a" + i);
                }
            }
        }
    }
}