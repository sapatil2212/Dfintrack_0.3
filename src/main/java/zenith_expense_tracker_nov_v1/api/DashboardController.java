package zenith_expense_tracker_nov_v1.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        // Return the view name for the dashboard page (e.g., "dashboard.html")
        return "dashboard"; // Make sure you have a dashboard.html or dashboard.jsp view in your resources
    }
}
