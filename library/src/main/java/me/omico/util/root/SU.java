package me.omico.util.root;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by yuwen on 17-1-20.
 */

public class SU {
    private Process process;
    private BufferedWriter mOutputWriter;
    private BufferedReader mInputReader;
    private boolean closed;
    private boolean denied;
    private boolean firstTry;
    private static SU su;

    private SU() {
        try {
            firstTry = true;
            process = Runtime.getRuntime().exec("su");
            mOutputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            mInputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException ignored) {
            denied = true;
            closed = true;
        }
    }

    public synchronized String runCommand(final String command) {
        synchronized (this) {
            try {
                StringBuilder mStringBuilder = new StringBuilder();
                String callback = "/shellCallback/";
                mOutputWriter.write(command + "\necho " + callback + "\n");
                mOutputWriter.flush();

                String line;
                while ((line = mInputReader.readLine()) != null) {
                    if (line.equals(callback)) {
                        break;
                    }
                    mStringBuilder.append(line).append("\n");
                }
                firstTry = false;
                return mStringBuilder.toString().trim();
            } catch (IOException e) {
                closed = true;
                e.printStackTrace();
                if (firstTry) denied = true;
            } catch (ArrayIndexOutOfBoundsException e) {
                denied = true;
            } catch (Exception e) {
                e.printStackTrace();
                denied = true;
            }
            return null;
        }
    }

    private void close() {
        try {
            mOutputWriter.write("exit\n");
            mOutputWriter.flush();
            process.waitFor();
            mOutputWriter.close();
            mInputReader.close();
            process.destroy();
            closed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SU getSU() {
        if (su == null || su.closed || su.denied) {
            if (su != null && !su.closed) {
                su.close();
            }
            su = new SU();
        }
        return su;
    }

    public static boolean isRoot() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            return process.exitValue() != 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
