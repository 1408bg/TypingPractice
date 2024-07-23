package App;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args){
        final HttpClient httpClient = HttpClient.newHttpClient();
        final Scanner sc = new Scanner(System.in);
        final Random rand = new Random();

        System.out.print("input your vocabulary list url (default : https://1408bg.github.io/assets/words.txt)\n>> ");
        String url = sc.nextLine();
        if (url.isEmpty()){
            url = "https://1408bg.github.io/assets/words.txt";
        }

        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        String[] words = new String[1];

        System.out.println("fetching...\n\n");

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                words = httpResponse.body().split(" ");
            } else {
                System.out.println("HTTP Request failed with status code: " + statusCode);
                System.exit(-1);
            }

        } catch (IOException | InterruptedException e) {
            words = new String[]{"null"};
            System.out.println("error: "+e.getMessage());
            System.exit(-1);
        }

        System.out.print("size (max : "+words.length+") << ");
        int size = sc.nextInt();
        sc.nextLine();
        int temp;
        if ((size > words.length) || (size < 1)){
            System.out.println("out of range");
            System.exit(-1);
        }
        String[] mainWords = new String[size];
        System.out.println("loading..");
        for (int i = 0; i < size; i++){
            temp = rand.nextInt();
            mainWords[i] = words[(temp > 0 ? temp : -temp)% words.length];
            if (i == 0) continue;
            for (int j = 0; j < (i-1); j++){
                if (mainWords[i].equals(mainWords[j])){
                    i--;
                    break;
                }
            }
        }
        int score = 0;
        int ac = 0;
        int count = 1;
        System.out.println("\n\n");
        Instant start = Instant.now();
        for (String s : mainWords){
            s = s.replace("\n", "");
            System.out.print(count++ +"\t"+s+"\n>>\t");
            if (sc.nextLine().equals(s)){
                ac++;
                score += s.length();
            }
        }
        Instant end = Instant.now();
        Duration runtime = Duration.between(start, end);
        System.out.println("\n\n[result]\nspeed : "+ (score * 100L) / (runtime.getSeconds()) +"\naccuracy : "+ (Math.round(((ac * 1.0) / size) * 100) / 100.00) * 100);
    }
}
