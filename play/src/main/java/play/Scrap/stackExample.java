package play.Scrap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class stackExample {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                frame.setPreferredSize(new Dimension(1024, 768));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel content = new JPanel();

                // Set up the graph and the display.
                int numV = 70;
                int numE = 50;
                EppsteinPowerLawGenerator<String, String> gen = new EppsteinPowerLawGenerator<String, String>(
                        new GraphFactory(), new CountFactory(),
                        new CountFactory(), numV, numE, 10);
                Graph<String, String> graph = gen.create();
                Layout<String, String> layout = new SpringLayout<String, String>(
                        graph);
                VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(
                        layout);
                vv.getRenderContext().setEdgeStrokeTransformer(
                        new Transformer<String, Stroke>() {

                            @Override
                            public Stroke transform(String edge) {
                                return new BasicStroke(1.5f);
                            }
                        });

                content.add(vv);

                frame.setContentPane(content);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Animate the edges!
                AnimationTimerTask at = new AnimationTimerTask(vv);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(at, 10, 30);
            }

        });
    }

    static class AnimationTimerTask extends TimerTask {

        private final double width = 0.1; // Size of the colored line.
        private final double stepsize = 0.01;
        private double keyframe = 0 + width; // Between 0.0 and 1.0
        private VisualizationViewer<String, String> vv = null;

        public AnimationTimerTask(VisualizationViewer<String, String> vv) {
            this.vv = vv;
        }

        @Override
        public void run() {
            vv.getRenderContext().setEdgeDrawPaintTransformer(
                    new Transformer<String, Paint>() {

                        @Override
                        public Paint transform(String edge) {
                            // Find both points of the edge.
                            Pair<String> vs = vv.getGraphLayout().getGraph()
                                    .getEndpoints(edge);
                            Point2D p1 = vv.getGraphLayout().transform(
                                    vs.getFirst());
                            Point2D p2 = vv.getGraphLayout().transform(
                                    vs.getSecond());

                            // This code won't handle self-edges.
                            if (p1.equals(p2)) {
                                return Color.red;
                            }

                            Color[] colors = { Color.gray, Color.red,
                                    Color.gray };
                            float start = (float) Math.max(0.0, keyframe
                                    - width);
                            float end = (float) Math.min(1.0, keyframe + width);
                            float[] fractions = { start, (float) keyframe, end };
                            return new LinearGradientPaint(p1, p2, fractions,
                                    colors);
                        }

                    });
            vv.repaint();
            keyframe += stepsize;
            keyframe %= 1.0;
        }
    }

    static class GraphFactory implements Factory<Graph<String, String>> {

        @Override
        public Graph<String, String> create() {
            return new SparseMultigraph<String, String>();
        }
    }

    static class CountFactory implements Factory<String> {

        private int count = 0;

        @Override
        public String create() {
            return String.valueOf(count++);
        }
    }
}