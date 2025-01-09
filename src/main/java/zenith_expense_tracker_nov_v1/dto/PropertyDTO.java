package zenith_expense_tracker_nov_v1.dto;

public class PropertyDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Long userId;
    private String userName;

    // Constructors, getters, setters
    public PropertyDTO() {}

    public PropertyDTO(Long id, String name, String address, String description, Long userId, String userName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.userId = userId;
        this.userName = userName;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
