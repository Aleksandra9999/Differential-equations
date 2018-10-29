package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;


public class Controller {

    @FXML
    LineChart<Number, Number> MyChart;

    @FXML
    LineChart<Number, Number> ErrorsChart;

    @FXML
    LineChart<Number, Number> Maxerrors;


    public TextField x0Input;
    public TextField y0Input;
    public TextField XInput;
    public TextField NInput;
    public TextField NminInput;
    public TextField NmaxInput;

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
    public static XYChart.Series maxerrorseuler;
    public static XYChart.Series maxerrorsimpeuler;
    public static XYChart.Series maxerrorsrungekutta;

    public static double eumax = 0, impeumax = 0, rkmax = 0;


    @FXML
    private void btn(ActionEvent actionEvent) {
        MyChart.getData().clear();
        MyChart.setCreateSymbols(false);
        ErrorsChart.getData().clear();
        ErrorsChart.setCreateSymbols(false);
        Maxerrors.getData().clear();
        Maxerrors.setCreateSymbols(false);
        double x0,y0,X;
        int N, Nmin, Nmax;
        x0 = Double.parseDouble(x0Input.getText().toString());
        y0 = Double.parseDouble(y0Input.getText().toString());
        X = Double.parseDouble(XInput.getText().toString());
        N = Integer.parseInt(NInput.getText().toString());
        Nmin = Integer.parseInt(NminInput.getText().toString());
        Nmax = Integer.parseInt(NmaxInput.getText().toString());

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


        eumax = 0;
        impeumax = 0;
        rkmax = 0;


        MaxErrors(x0, y0, X, Nmin, Nmax);

            maxerrorseuler.setName("Euler's method");
            Maxerrors.getData().add(maxerrorseuler);

            maxerrorsimpeuler.setName("Improved Euler's method");
            Maxerrors.getData().add(maxerrorsimpeuler);

            maxerrorsrungekutta.setName("Runge-Kutta method");
            Maxerrors.getData().add(maxerrorsrungekutta);


    }


    public static XYChart.Series Euler(double x0, double y0, double x, int N) {
        double curX, curY;
        eumax = 0;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        errorseuler = new XYChart.Series();
        Double h = Math.abs(x - x0) / N;


        while (curX <= x) {
            serie.getData().add(new XYChart.Data<>(curX, curY));
            double temp = Math.abs(curY - function(curX, x0, y0));
            errorseuler.getData().add(new XYChart.Data<>(curX, temp));
            if (eumax < temp) eumax = temp;
            curY = (double) Math.round(curY *100000000)/100000000;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY += h * funcxy(curX, curY);
            curX += h;
        }

        return serie;
    }


    public static XYChart.Series ImpEuler(double x0, double y0, double x, int N) {
        double curX = x0;
        double curY = y0;
        impeumax = 0;
        XYChart.Series serie = new XYChart.Series();
        errorsimpeuler = new XYChart.Series();
        double h = Math.abs(x - x0) / N;

        double a,b;

        while (curX <= x) {

            serie.getData().add(new XYChart.Data<>(curX, curY));
            double temp = Math.abs(curY - function(curX, x0, y0));

            errorsimpeuler.getData().add(new XYChart.Data<>(curX, temp));
            if (impeumax < temp) impeumax = temp;

            a = funcxy(curX,curY);
            b = h*funcxy(curX+h/2, curY+ h*a/2);
            curX += h;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY = (double) Math.round(curY *100000000)/100000000;
            curY += b;
        }

        return serie;
    }


    public static XYChart.Series RungeKutta(double x0, double y0, double x, int N) {
        double curX, curY, k1, k2, k3, k4;
        rkmax = 0;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        errorsrungekutta = new XYChart.Series();
        double h = Math.abs(x - x0) / N;

        while (curX <= x) {
            serie.getData().add(new XYChart.Data<>(curX, curY));
            double temp = Math.abs(curY - function(curX, x0, y0));
            errorsrungekutta.getData().add(new XYChart.Data<>(curX, temp));
            if (rkmax < temp) rkmax = temp;

            k1 = funcxy(curX, curY);
            k2 = funcxy(curX + h * 0.5, curY + h * k1 * 0.5);
            k3 = funcxy(curX + h * 0.5, curY + h * k2 * 0.5);
            k4 = funcxy(curX + h, curY + h * k3);

            curY += h / 6 * (k1 + 2 * k2 + 2 * k3 + k4);
            curX += h;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY = (double) Math.round(curY *100000000)/100000000;
        }

        return serie;
    }


    public static XYChart.Series Analytical(double x0, double y0, double x, int N) {
        double curX, curY;
        curX = x0;
        curY = y0;
        XYChart.Series serie = new XYChart.Series();
        double h = Math.abs(x - x0) / N;

        while (curX <= x) {

            serie.getData().add(new XYChart.Data<>(curX, curY));
            curX += h;
            curX = (double) Math.round(curX *100000000)/100000000;
            curY = function(curX, x0, y0);
            curY = (double) Math.round(curY *100000000)/100000000;
        }

        return serie;
    }


    public static double funcxy(double x, double y) {
        return ((4/(x * x)) -  (y/x) - (y * y));
    }


    public static double function(double x, double x0, double y0){
        double c;
        c = (y0 + 2)/(4*y0*Math.pow(x0, 5) - 8*Math.pow(x0, 4));
        return ((2+8*c*Math.pow(x, 4))/(x*(4*c*Math.pow(x, 4)-1)));
    }


    public static void MaxErrors(double x0, double y0, double x, int Nmin, int Nmax){
        maxerrorseuler = new XYChart.Series();
        maxerrorsimpeuler = new XYChart.Series();
        maxerrorsrungekutta = new XYChart.Series();
        for (int i = Nmin; i <= Nmax; i++){
            Euler(x0, y0, x, i);
            maxerrorseuler.getData().add(new XYChart.Data<>(i, eumax));
            ImpEuler(x0, y0, x, i);
            maxerrorsimpeuler.getData().add(new XYChart.Data<>(i, impeumax));
            RungeKutta(x0, y0, x, i);
            maxerrorsrungekutta.getData().add(new XYChart.Data<>(i, rkmax));
        }
    }
}
