package bd.edu.seu.futuresalespredictionjava.service;

import bd.edu.seu.futuresalespredictionjava.dto.PredictionTrendDto;
import bd.edu.seu.futuresalespredictionjava.dto.SalesPredictionDto;
import bd.edu.seu.futuresalespredictionjava.model.Prediction;
import bd.edu.seu.futuresalespredictionjava.model.SalesEntry;
import bd.edu.seu.futuresalespredictionjava.repository.PredictionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PredictionService {
    private final ChatClient chatClient;
    private final PredictionRepository predictionRepository;

    public PredictionService(ChatClient.Builder chatClientBuilder, PredictionRepository predictionRepository) {
        // ChatClient toiri thakbe jate Spring Boot run time e kono error na dey
        this.chatClient = chatClientBuilder.build();
        this.predictionRepository = predictionRepository;
    }

    // 1. Future Sales Prediction (Dummy Logic)
    public String predictSales(SalesPredictionDto dto) {
        /* * Original API call comment kore deya holo sir ke dummy data dekhanor jonno.
         * Pore API add korte chaile eta abar uncomment korte parba.
         */

        StringBuilder dummyResponse = new StringBuilder();

        dummyResponse.append("<h3>Future Sales Prediction</h3>");
        dummyResponse.append("<p>Based on the historical data for <strong>").append(dto.getProductName()).append("</strong>, we anticipate a steady upward trend. Expected sales for the next week are projected to increase by <strong>10% - 15%</strong> compared to the recent average.</p>");

        dummyResponse.append("<h3>Impact of Weather on Sales</h3>");
        dummyResponse.append("<ul>");
        dummyResponse.append("<li><strong>Clear/Sunny Days:</strong> Expected to drive high customer engagement. Sales might spike by 20%.</li>");
        dummyResponse.append("<li><strong>Rainy/Bad Weather:</strong> Physical store visits may drop, but online orders are likely to see a significant boost.</li>");
        dummyResponse.append("</ul>");

        dummyResponse.append("<h3>Suggestions to Increase Sales</h3>");
        dummyResponse.append("<ul>");
        dummyResponse.append("<li>Run a limited-time 'Weekend Flash Sale' to attract more buyers.</li>");
        dummyResponse.append("<li>Increase social media marketing focusing on the product's unique features.</li>");
        dummyResponse.append("<li>Offer bundle deals (e.g., Buy 1 Get 1 at 50% off) to clear out existing inventory.</li>");
        dummyResponse.append("</ul>");

        return dummyResponse.toString();
    }

    // Graph er jonno Data Save (Already dummy 10% kora chilo, setai rakhlam)
    public void savePredictionForUser(String userId, SalesPredictionDto dto) {
        Map<String, Integer> actualSales = new HashMap<>();
        Map<String, Integer> predictedSales = new HashMap<>();

        dto.getEntries().forEach(entry -> {
            if (entry.getDate() != null) {
                String dateStr = entry.getDate().toString();
                actualSales.put(dateStr, entry.getUnitsSold());

                // +10% prediction calculation
                int predictedValue = (int) Math.round(entry.getUnitsSold() * 1.1);
                predictedSales.put(dateStr, predictedValue);
            }
        });

        Prediction prediction = new Prediction(
                userId,
                dto.getProductName(),
                LocalDate.now(),
                actualSales,
                predictedSales
        );

        predictionRepository.save(prediction);
    }

    public List<Prediction> getAllPredictionsForUser(String userId) {
        return predictionRepository.findAllByUserId(userId);
    }

    public List<PredictionTrendDto> getTrendsForUser(String userId) {
        List<Prediction> predictions = predictionRepository.findAllByUserId(userId);
        Map<String, PredictionTrendDto> trendMap = new TreeMap<>();

        for (Prediction prediction : predictions) {
            for (Map.Entry<String, Integer> entry : prediction.getPredictedSales().entrySet()) {
                String date = entry.getKey();
                Integer predicted = entry.getValue();
                Integer actual = prediction.getActualSales().getOrDefault(date, 0);
                trendMap.put(date, new PredictionTrendDto(date, actual, predicted));
            }
        }
        return new ArrayList<>(trendMap.values());
    }

    // 2. Image Prediction (Dummy Logic)
    public String analyzeImage(byte[] imageBytes) {
        /*
         * Original Image Analyze API call comment kore dummy data deya holo.
         */

        StringBuilder dummyResponse = new StringBuilder();

        dummyResponse.append("<h3>Product Image Analysis</h3>");
        dummyResponse.append("<p>Based on the provided product image, here are our AI-driven suggestions to improve its market appeal:</p>");

        dummyResponse.append("<ul>");
        dummyResponse.append("<li><strong>Color Grading:</strong> The colors could be slightly more vibrant. Consider offering variants in Navy Blue or Maroon to target a younger demographic.</li>");
        dummyResponse.append("<li><strong>Design & Logo Placement:</strong> The logo placement is decent, but moving it slightly higher or making it more prominent could increase brand visibility.</li>");
        dummyResponse.append("<li><strong>Presentation:</strong> Using a solid, light-colored background (like pure white or light grey) will make the product pop out more in e-commerce listings.</li>");
        dummyResponse.append("<li><strong>Material Texture:</strong> The material looks good, but ensuring proper lighting in the photoshoot will highlight the premium quality of the fabric.</li>");
        dummyResponse.append("</ul>");

        return dummyResponse.toString();
    }
}