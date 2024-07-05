package dev.promise.greeting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GreetingService {

    public Map<String, String> greet(String visitorName) {

        Map<String,String> clientDetails  = getClientIPAndLocation();
        String clientIp = clientDetails.getOrDefault("ip","-");

        log.info("Client IP returned ===> {}",clientIp);
        String tempInCelsius = clientDetails.getOrDefault("temp","-");
        String city = clientDetails.getOrDefault("city","-");

        Map<String,String> response = new HashMap<>();
        response.put("client_ip",clientIp);
        response.put("location",city);
        String greeting = "Hello, "+visitorName+ "!, the temperature is "+tempInCelsius+ " degree celsius in "+city;
        response.put("greeting",greeting);



        return response;
    }

    private Map<String,String> getClientIPAndLocation() {
        String url = "http://ipinfo.io/json";
        Map<String,String> details = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
            JSONObject object = new JSONObject(response.getBody());
            String ip  = object.optString("ip");
            String city = object.optString("city");
            details.put("ip",ip);
            details.put("city",city);
            String temperature = getTemperatureFromCity(city);
            details.put("temp",temperature);
            return details;
        }else return new HashMap<>();
    }

    private String getTemperatureFromCity(String city) {
        String url = "https://wttr.in/"+city+"?format=j1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
            JSONObject object = new JSONObject(response.getBody());
            var currentCondition = object.optJSONArray("current_condition");
            JSONObject tempDetails = currentCondition.optJSONObject(0,new JSONObject());
            String temp = tempDetails.optString("temp_C");
            return temp;
        }else return "";

    }
}
