import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stickman extends JPanel implements ActionListener {

    // アニメーション用の時間変数
    double time = 0;
    Timer timer;

    public Stickman() {
        // 約60FPSで画面を更新するタイマー
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // 描画を滑らかにするアンチエイリアス設定
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shoulderY = 150;
        int lShoulderX = 170;
        int rShoulderX = 230;

        // 時間(time)に合わせて両腕の角度を変化
        double lUpperArmAngle = 110 + 20 * Math.sin(time);
        double lLowerArmAngle = 50 + 30 * Math.sin(time);
        
        // 右腕は左腕と逆の位相（+Math.PI）で振る
        double rUpperArmAngle = 70 + 20 * Math.sin(time + Math.PI);
        double rLowerArmAngle = 130 + 30 * Math.sin(time + Math.PI);

        // --- 人体の描画 ---
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        
        // 頭
        g2.drawOval(175, 50, 50, 50);
        
        // 胴体
        g2.drawLine(200, 100, 200, 250);
        
        // 肩幅
        g2.drawLine(lShoulderX, shoulderY, rShoulderX, shoulderY);

        // 足
        g2.drawLine(200, 250, 160, 350);
        g2.drawLine(200, 250, 240, 350);

        // --- 左腕の計算と描画 ---
        int lElbowX = lShoulderX + (int)(60 * Math.cos(Math.toRadians(lUpperArmAngle)));
        int lElbowY = shoulderY + (int)(60 * Math.sin(Math.toRadians(lUpperArmAngle)));
        int lHandX = lElbowX + (int)(60 * Math.cos(Math.toRadians(lLowerArmAngle)));
        int lHandY = lElbowY + (int)(60 * Math.sin(Math.toRadians(lLowerArmAngle)));
        g2.drawLine(lShoulderX, shoulderY, lElbowX, lElbowY);
        g2.drawLine(lElbowX, lElbowY, lHandX, lHandY);

        // --- 右腕の計算と描画 ---
        int rElbowX = rShoulderX + (int)(60 * Math.cos(Math.toRadians(rUpperArmAngle)));
        int rElbowY = shoulderY + (int)(60 * Math.sin(Math.toRadians(rUpperArmAngle)));
        int rHandX = rElbowX + (int)(60 * Math.cos(Math.toRadians(rLowerArmAngle)));
        int rHandY = rElbowY + (int)(60 * Math.sin(Math.toRadians(rLowerArmAngle)));
        g2.drawLine(rShoulderX, shoulderY, rElbowX, rElbowY);
        g2.drawLine(rElbowX, rElbowY, rHandX, rHandY);

        // --- ハンドスティック(HS)の描画 ---
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(4));
        
        double lHsAngle = lLowerArmAngle - 20;
        int lHsTipX = lHandX + (int)(60 * Math.cos(Math.toRadians(lHsAngle)));
        int lHsTipY = lHandY + (int)(60 * Math.sin(Math.toRadians(lHsAngle)));
        g2.drawLine(lHandX, lHandY, lHsTipX, lHsTipY);

        double rHsAngle = rLowerArmAngle + 20;
        int rHsTipX = rHandX + (int)(60 * Math.cos(Math.toRadians(rHsAngle)));
        int rHsTipY = rHandY + (int)(60 * Math.sin(Math.toRadians(rHsAngle)));
        g2.drawLine(rHandX, rHandY, rHsTipX, rHsTipY);

        // --- デビルスティックの描画 ---
        g2.setColor(new Color(34, 139, 34)); // 少し濃い緑色
        g2.setStroke(new BasicStroke(6));
        
        // 左右のハンドスティックの間を行き来するモーション
        double t = (Math.sin(time) + 1) / 2.0; // 0.0〜1.0の間を往復
        int dsX = (int)(lHsTipX + (rHsTipX - lHsTipX) * t);
        int dsY = (int)(lHsTipY + (rHsTipY - lHsTipY) * t) - 30; // 少し上に浮かせる
        
        double dsAngle = time * 3; // 回転速度
        int dsDx = (int)(40 * Math.cos(dsAngle));
        int dsDy = (int)(40 * Math.sin(dsAngle));
        g2.drawLine(dsX - dsDx, dsY - dsDy, dsX + dsDx, dsY + dsDy);

        // --- 関節の表示 ---
        g2.setColor(Color.BLUE);
        g2.fillOval(lHandX - 4, lHandY - 4, 8, 8);
        g2.fillOval(rHandX - 4, rHandY - 4, 8, 8);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time += 0.05; // 時間を進める
        repaint();    // 画面を再描画
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stickman Juggling Devilstick");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.add(new Stickman());
        frame.setVisible(true);
    }
}