package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class Controller {

    @FXML
    LineChart<Number, Number> MyChart;

    @FXML
    LineChart<Number, Number> ErrorsChart;


    public TextField x0Input;
    public TextField y0Input;
    public TextField XInput;
    public TextField NInput;

    public CheckBox eulerbox;
    public CheckBox impeulerbox;
    public CheckBox rungekuttabox;
    public CheckBox analytbox;

    public XYChart.Series euler;
    public XYChart.Series impeuler;
    public XYChart.Series rungekutta;
    public XYChart.Series analytical;
    public static XYChart.Series errorseuler;
    public static XYChart.Series errorsimpeuler;
    public static XYChart.Series errorsrungekutta;


    @FXML
    private void btn(ActionEvent actionEvent) {
        MyChart.getData().clear();
        MyChart.setCreateSymbols(false);
        ErrorsChart.getData().clear();
        ErrorsChart.setCreateSymbols(false);
        double x0,y0,X;
        int N;
        x0 = Double.parseDouble(x0Input.getText());
        y0 = Double.parseDouble(y0Input.getText());
        X = Double.parseDouble(XInput.getText());
        N = Integer.parseInt(NInput.getText());

        euler = Euler(x0, y0, X, N);
        impeuler = ImpEuler(x0, y0, X, N);
        rungekutta = RungeKutta(x0, y0, X, N);
        analytical = Analytical(x0, y0, X, N);

        if (eulerbox.isSelected()) {
            euler.setName("Euler's method");
            errorseuler.setName("Euler's method");
            MyChart.getData().add(euler);
            ErrorsChart.getData().add(errorseuler);
        }

        if (impeulerbox.isSelected()) {
            impeuler.setName("Improved Euler's method");
            errorsimpeuler.setName("Improved Euler's method");
            MyChart.getData().add(impeuler);
            ErrorsChart.getData().add(errorsimpeuler);
        }

        if (rungekuttabox.isSelected()) {
            rungekutta.setName("Runge-Kutta method");
            errorsrungekutta.setName("Runge-Kutta method");
            MyChart.getData().add(rungekutta);
            ErrorsChart.getData().add(errorsrungekutta);
        }

        if (analytbox.isSelected()) {
            analytical.setName("Analitical method");
            MyChart.getData().add(analytical);
        }


    }


    public static XYChart.Series Euler(double x0, double y0, double x, double N) {
        double curX, curY;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        errorseuler = new XYChart.Series();
        Double h = Math.abs(x - x0) / N;
        System.out.println(h);


        while (curX <= x) {
            serie.getData().add(new XYChart.Data<>(curX, curY));
            errorseuler.getData().add(new XYChart.Data<>(curX, Math.abs(curY - function(curX))));
            curY += h * funcxy(curX, curY);
            curX += h;
            curX = (double) Math.round(curX *100000000)/100000000;
        }

        return serie;
    }


    public static XYChart.Series ImpEuler(double x0, double y0, double x, double N) {
        double curX = x0;
        double curY = y0;
        XYChart.Series serie = new XYChart.Series();
        errorsimpeuler = new XYChart.Series();
        double n = Math.abs(x - x0) / N;

        double a,b;

        while (curX <= x) {

            serie.getData().add(new XYChart.Data<>(curX, curY));
            errorsimpeuler.getData().add(new XYChart.Data<>(curX, Math.abs(curY - function(curX))));

            a = funcxy(curX,curY);
            b = n*funcxy(curX+n/2, curY+ n*a/2);
            curX += n;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY += b;
        }

        return serie;
    }


    public static XYChart.Series RungeKutta(double x0, double y0, double x, double N) {
        double curX, curY, k1, k2, k3, k4;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        errorsrungekutta = new XYChart.Series();
        double n = Math.abs(x - x0) / N;

        while (curX <= x) {
            serie.getData().add(new XYChart.Data<>(curX, curY));
            errorsrungekutta.getData().add(new XYChart.Data<>(curX, Math.abs(curY - function(curX))));

            k1 = funcxy(curX, curY);
            k2 = funcxy(curX + n * 0.5, curY + n * k1 * 0.5);
            k3 = funcxy(curX + n * 0.5, curY + n * k2 * 0.5);
            k4 = funcxy(curX + n, curY + n * k3);

            curY += n / 6 * (k1 + 2 * k2 + 2 * k3 + k4);
            curX += n;
            curX = (double) Math.round(curX *100000000)/100000000;
        }

        return serie;
    }


    public static XYChart.Series Analytical(double x0, double y0, double x, double N) {
        double curX, curY;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        double n = Math.abs(x - x0) / N;

        while (curX <= x) {

            serie.getData().add(new XYChart.Data<>(curX, curY));
            curX += n;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY = function(curX);
        }

        return serie;
    }


    public static double funcxy(double x, double y) {
        return ((4/(x * x)) - (y/x) - (y * y));
    }


    public static double function(double x){
        return (4/(5*Math.pow(x, 5) - x) + 2/x);
    }
}
