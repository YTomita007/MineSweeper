import java.awt.*;
import javax.swing.*;

public class MineResult extends JFrame{
    
    JPanel panel = new JPanel();
    Image img;
    JLabel picLabel;

    // 開発用に作成（実際のプレーではインスタンス化して使用するのでここから起動しない）
    // public static void main(String[] args) {
    //     MineResult result = new Result("result", 1);
    // }

    MineResult(String title, int pattern){
        setTitle(title);

        if(pattern == 1){
            img = Toolkit.getDefaultToolkit().getImage("images/Victory.png");
            picLabel = new JLabel(new ImageIcon(img));    
        }else{
            img = Toolkit.getDefaultToolkit().getImage("images/GameOver.png");
            picLabel = new JLabel(new ImageIcon(img));    
        }
    
        getContentPane().add(panel.add(picLabel));
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
