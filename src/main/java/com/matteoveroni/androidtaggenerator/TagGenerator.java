package com.matteoveroni.androidtaggenerator;

import com.matteoveroni.myutils.Str;

import java.util.UUID;

/**
 *
 * @author Matteo Veroni
 *
 * @version 1.0
 *
 */
public final class TagGenerator {

    public static final String TAG_PREFIX = "Tag";
    public static final int MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG = 23;

    private volatile static TagGenerator TAG_GENERATOR_INSTANCE;

    private final CamelCaseStringShrinker camelCaseStringShrinker = new CamelCaseStringShrinker();

    private TagGenerator() {
    }

    public static final String tag(Class classInstance) {
        if (TAG_GENERATOR_INSTANCE == null) {
            synchronized (TagGenerator.class) {
                if (TAG_GENERATOR_INSTANCE == null) {
                    TAG_GENERATOR_INSTANCE = new TagGenerator();
                }
            }
        }
        return TAG_GENERATOR_INSTANCE.create(classInstance);
    }

    /**
     * Method which generates Android Tags for a given class. Android TAG's
     * length can't exceed 23 characters.
     *
     * @return the corresponding TAG_PREFIX for class passed to the constructor
     */
    private String create(Class classInstance) {
        String tag = classInstance.getSimpleName();
        int tagLength = tag.length();
        if (tagLength < 1) {
            tag = generateRandomUniqueTag();
        } else {
            if (!Character.isUpperCase(tag.charAt(0))) {
                tag = Str.capitalizeFirstLetter(tag);
            }
            if (tagLength > MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG) {
                tag = camelCaseStringShrinker.shrink(tag, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
            }
        }
        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // HELPER METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Useful material:
     * <p>
     * http://stackoverflow.com/questions/20994768/how-to-reduce-length-of-uuid-generated-using-randomuuid
     * </p>
     */
    private String generateRandomUniqueTag() {
        final UUID uuid = UUID.randomUUID();
        String str_uuid = Long.toString(uuid.getLeastSignificantBits(), 94);
        return TAG_PREFIX + str_uuid;
    }
}
