package bgu.spl.mics.application.passiveObjects;
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private String jsonString="";
    private AtomicInteger totalAttacks;
    private Vector<Task> data;

    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }
    private Diary(){
        totalAttacks=new AtomicInteger(0);
        data=new Vector<Task>();
    }

    public static Diary getInstance() {
        return DiaryHolder.instance;
    }

    public void updateTotalAttacks() {
        while (!totalAttacks.compareAndSet(totalAttacks.intValue(), totalAttacks.intValue() + 1)) {};
    }

    public void addTask(Task task){
        data.add(task);
    }

    public int getTotalAttacks() {
        return totalAttacks.intValue();
    }

    public long getC3POFinish() {
        long finished = 0;
        long started = 0;
        for (Task task: data){
            if(task.getTaskDescription().equals("Finished")&task.getExecuterName().equals("C3PO")){
                finished = Math.max(finished,task.getTimestamp());
            }
        }
        for (Task task: data){
            if(task.getTaskDescription().equals("Started")&task.getExecuterName().equals("C3PO")){
                started = task.getTimestamp();
            }
        }
        return (finished-started);
    }

    public long getHanSoloFinish() {
        long finished = 0;
        long started = 0;
        for (Task task: data){
            if(task.getTaskDescription().equals("Finished")&task.getExecuterName().equals("Han")){
                finished = Math.max(finished,task.getTimestamp());
            }
        }
        for (Task task: data){
            if(task.getTaskDescription().equals("Started")&task.getExecuterName().equals("Han")){
                started = task.getTimestamp();
            }
        }
        return (finished-started);
    }

    public long getR2D2Deactivate() {
        for (Task task: data){
            if (task.getExecuterName().equals("R2D2")&task.getTaskDescription().equals("Deactivated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }

    public long getLeiaTerminate() {
        for (Task task: data){
            if (task.getExecuterName().equals("Leia")&task.getTaskDescription().equals("Terminated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }

    public long getHanSoloTerminate() {
        for (Task task: data){
            if (task.getExecuterName().equals("Han")&task.getTaskDescription().equals("Terminated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }

    public long getC3POTerminate() {
        for (Task task: data){
            if (task.getExecuterName().equals("C3PO")&task.getTaskDescription().equals("Terminated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }

    public long getR2D2Terminate() {
        for (Task task: data){
            if (task.getExecuterName().equals("R2D2")&task.getTaskDescription().equals("Terminated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }

    public long getLandoTerminate() {
        for (Task task: data){
            if (task.getExecuterName().equals("Lando")&task.getTaskDescription().equals("Terminated")){
                return task.getTimestamp();
            }
        }
        return 0;
    }
    public void resetNumberAttacks(){this.totalAttacks.getAndSet(0);}
    public void writeOutput(String path) {
        JsonArray jsonDiaryArray = new JsonArray();
        jsonDiaryArray.add("Total attacks: " + getTotalAttacks());
        jsonDiaryArray.add("Han-Solo finished his attacks after: " + getHanSoloFinish() + " milliseconds");
        jsonDiaryArray.add("C3PO finished his attacks after: " + getC3POFinish() + " milliseconds");
        jsonDiaryArray.add("R2D2 deactivated the shield generator after: " + getR2D2Deactivate() + " milliseconds");
        jsonDiaryArray.add("Leia was terminated after " + getLeiaTerminate() + "milliseconds");
        jsonDiaryArray.add("Han-Solo was terminated after " + getHanSoloTerminate() + " milliseconds");
        jsonDiaryArray.add("C3PO was terminated after " + getC3POTerminate() + " milliseconds");
        jsonDiaryArray.add("R2D2 was terminated after " + getR2D2Terminate() + "milliseconds");
        jsonDiaryArray.add("Lando was terminated after " + getLandoTerminate() + " milliseconds");
        try {
//            File output=new File(filename+".json");
//            output.createNewFile();
//            FileWriter fileWriter = new FileWriter(output.getName());
//            fileWriter.write(jsonDiaryArray.toString());


//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            JsonParser jp = new JsonParser();
//            JsonElement je = jp.parse(String.valueOf(jsonDiaryArray));
//            String prettyJsonString = gson.toJson(je);
//
//            Files.write(Paths.get(filename), prettyJsonString.getBytes());

            Gson g = new Gson();
            Writer writer = new FileWriter(path);
            g.toJson(jsonDiaryArray,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
