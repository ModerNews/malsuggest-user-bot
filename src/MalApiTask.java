package src;

import java.io.IOException;
import java.util.*;

public class MalApiTask extends Thread{
    private ApiCaller caller;
    private String access_token;

    public static void main(String[] args) throws IOException, InterruptedException {
        new MalApiTask(args[0]);
    }

    public MalApiTask(String access_token) throws IOException, InterruptedException {
        this.access_token = access_token;
        this.caller = new ApiCaller(access_token);
        cleanup();
        first_setup();
    }

    public void run(){
        do {
            try {
                Thread.sleep(8 * 60000);
                // TODO: Drop 100 completed titles
                HashMap<String, Object> my_list = caller.get_my_animelist("completed");
                ArrayList<HashMap> my_data = (ArrayList<HashMap>) my_list.get("data");
                System.out.println("Dropping 100 completed titles from user's animelist:");
                drop_entries(my_data, 100);

                //Adding new entries
                HashMap<String, Object> list = caller.get_anime_ranking(500, new Random().nextInt(0,501));
                ArrayList<HashMap> data = (ArrayList<HashMap>) list.get("data");
                System.out.println("Updating user's animelist with 100 completed titles: ");
                add_entries(data, 100, "completed");
            } catch (Exception ignore){}
        } while(true);
    }

    public void drop_entries(ArrayList<HashMap> data, int num_of_entries) throws IOException, InterruptedException {
        for (int i = 0; i <= num_of_entries; i++) {
            System.out.print(generate_staus_bar("Dropping", i, num_of_entries));
            int index = new Random().nextInt(data.size());
            HashMap<String, Object> temp = data.get(index);
            data.remove(index);
            HashMap node = (HashMap) temp.get("node");
            caller.delete_my_anime_list((int) node.get("id"));
            Thread.sleep(500);
        }
        System.out.println(generate_staus_bar("Dropping", num_of_entries, num_of_entries));
    }

    public String generate_staus_bar(String name, int progress, int total) {
        int length = 20;
        int progress_chars = (int) (((double)progress / total) * 20);
        String bar = "|" + "=".repeat(progress_chars) + ">" + " ".repeat(length - progress_chars) + "|";

        return name + "\t" + (int) (((double) progress / total) * 100) + "% " + bar + " (" + progress + "/" + total + ")\r";
    }

    public void add_entries(ArrayList<HashMap> data, int num_of_entries, String status) throws InterruptedException, IOException {
        for (int i = 0; i <= num_of_entries; i++) {
            System.out.print(generate_staus_bar(status, i, num_of_entries));
            int index = new Random().nextInt(data.size());
            HashMap<String, Object> temp = data.get(index);
            data.remove(index);
            HashMap node = (HashMap) temp.get("node");
            caller.update_my_anime_list((int) node.get("id"), status, false, new Random().nextInt(7, 11), (int) node.get("num_episodes"));
            Thread.sleep(500);
        }
        System.out.println(generate_staus_bar(status, num_of_entries, num_of_entries));
    }

    public void first_setup() throws IOException, InterruptedException {
        HashMap<String, Object> list = caller.get_anime_ranking(500);
        ArrayList<HashMap> data = (ArrayList<HashMap>) list.get("data");
        HashMap<String, Integer> to_fill = new HashMap<>();
        to_fill.put("completed", 250);
        to_fill.put("dropped", 10);
        to_fill.put("plan_to_watch", 100);
        to_fill.put("watching", 5);
        for(Map.Entry<String, Integer> status: to_fill.entrySet()) {
            System.out.println("Updating user's animelist with " + status.getValue() + " " + status.getKey() +  " titles: ");
            add_entries(data, status.getValue(), status.getKey());
        }
    }

    public void cleanup() throws IOException, InterruptedException {
        HashMap<String, Object> my_list = caller.get_my_animelist();
        if (my_list.size() <= 0) { return; }
        ArrayList<HashMap> my_data = (ArrayList<HashMap>) my_list.get("data");
        drop_entries(my_data, my_data.size() - 1);
    }
}
