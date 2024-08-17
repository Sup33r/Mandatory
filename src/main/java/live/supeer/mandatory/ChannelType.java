package live.supeer.mandatory;

import lombok.Getter;

@Getter
public class ChannelType {

    public static final ChannelType GLOBAL = new ChannelType("global");
    public static final ChannelType MOD = new ChannelType("mod");
    public static final ChannelType CITY = new ChannelType("city");
    public static final ChannelType OTHER = new ChannelType("other");

    private final String type;

    public ChannelType(String type) {
        this.type = type;
    }

    public static ChannelType getByName(String type) {
        return switch (type) {
            case "global" -> GLOBAL;
            case "mod" -> MOD;
            case "city" -> CITY;
            default -> OTHER;
        };
    }

}
