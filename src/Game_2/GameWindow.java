package Game_2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    private static GameWindow game_window;
    private static long last_frame_time; // подсчет времени
    private static Image background;
    private static Image game_over;
    private static Image drop;
    private static float drop_left = 200;
    private static float drop_top = 200;
    private static float drop_v = 200; // скорость drop
    private static int score; // счетчик

    public static void main(String[] args) throws IOException {
        // вставка изображения
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        //создание окна
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(0,0); // точка в которой появится окно
        game_window.setSize(1920,1080); // размер окна
        game_window.setResizable(false); // запрет на изменение окна мышкой
        GameField game_field = new GameField();
        // отлавливаем нажатие мыши
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // координаты
                int x = e.getX();
                int y = e.getY();
                // параметры нашей каплей
                float drop_right = drop_left + drop.getWidth(null); // правый
                float drop_bottom = drop_top + drop.getHeight(null); // нижний
                // попадение точки х y на каплю
                boolean is_drop = x>= drop_left && x<=drop_right && y>=drop_top && y<=drop_bottom;
                if(is_drop){
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null))); // рандомный радиус
                    drop_v = drop_v +20; // ускорение
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });

        last_frame_time = System.nanoTime();
        game_window.add(game_field);    // создаем панель
        game_window.setVisible(true); // делаем окно видимым
    }
    private  static void onRepaint(Graphics g){
        long current_time = System.nanoTime(); // текущее время
        float delta_time = (current_time - last_frame_time) * 0.000000001f; // посчитаем разницу во времени и перевод
        last_frame_time = current_time; // предыдущее = текущее

        drop_top = drop_top + drop_v * delta_time;

        g.drawImage(background, 0,0,null);
        g.drawImage(drop,(int) drop_left,(int) drop_top,null);
        if (drop_top > game_window.getHeight()) g.drawImage(game_over,800,400,null);
    }
    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g); // отрисует наш панель
            onRepaint(g);
            repaint(); // чаще
        }
    }
}
