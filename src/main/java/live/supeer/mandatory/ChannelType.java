package live.supeer.mandatory;

import lombok.Getter;

@Getter
public class ChannelType {

    public static final ChannelType PUBLIC = new ChannelType("public");
    public static final ChannelType PRIVATE = new ChannelType("private");
    public static final ChannelType CITY = new ChannelType("city");

    private final String type;

    public ChannelType(String type) {
        this.type = type;
    }

    public static ChannelType getByName(String type) {
        return switch (type) {
            case "public" -> PUBLIC;
            case "private" -> PRIVATE;
            case "city" -> CITY;
            default -> null;
        };
    }
}
