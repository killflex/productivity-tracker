package tugaspbo;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SwingHelper {
    public static void onChange(JSpinner spinner, ChangeListener listener, AtomicBoolean skip) {
        spinner.addChangeListener(e -> {
            if (skip.get()) {
                return;
            }

            skip.set(true);

            listener.stateChanged(e);

            skip.set(false);
        });
    }

    public static void onClick(JButton button, ActionListener listener, AtomicBoolean skip) {
        button.addActionListener(e -> {
            if (skip.get()) {
                return;
            }

            skip.set(true);

            listener.actionPerformed(e);

            skip.set(false);
        });
    }

    public static int validasiSpinnerJam(JSpinner spinnerJam, JSpinner spinnerMenit) {
        int jam = (int) spinnerJam.getValue();
        int menit = (int) spinnerMenit.getValue();

        if (jam < 0) {
            spinnerJam.setValue(jam = 23);
        } else if (jam > 23) {
            spinnerJam.setValue(jam %= 24);
        }

        return jam * 60 + menit;
    }

    public static int validasiSpinnerMenit(JSpinner spinnerJam, JSpinner spinnerMenit) {
        int jam = (int) spinnerJam.getValue();
        int menit = (int) spinnerMenit.getValue();

        if (menit < 0) {
            if (jam == 0) {
                jam = 23;
            } else {
                jam = ((int) ((float) jam + (menit / 60f))) % 24;
            }
            menit = 60 + (menit % 60);

            spinnerJam.setValue(jam);
            spinnerMenit.setValue(menit);
        } else if (menit > 59) {
            jam = (jam + menit / 60) % 24;
            menit %= 60;

            spinnerJam.setValue(jam);
            spinnerMenit.setValue(menit);
        }

        return jam * 60 + menit;
    }

    public static int waktu(JSpinner spinnerJam, JSpinner spinnerMenit) {
        int selesaiJam = (int) spinnerJam.getValue();
        int selesaiMenit = (int) spinnerMenit.getValue();

        return selesaiJam * 60 + selesaiMenit;
    }
}
