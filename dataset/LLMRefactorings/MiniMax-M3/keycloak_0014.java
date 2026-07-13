public class keycloak_0014 {

    public static long getClaimsMask(ClaimRepresentation rep) {
        long mask = ClaimMask.ALL;

        mask = applyClaim(mask, rep.getAddress(), ClaimMask.ADDRESS);
        mask = applyClaim(mask, rep.getEmail(), ClaimMask.EMAIL);
        mask = applyClaim(mask, rep.getGender(), ClaimMask.GENDER);
        mask = applyClaim(mask, rep.getLocale(), ClaimMask.LOCALE);
        mask = applyClaim(mask, rep.getName(), ClaimMask.NAME);
        mask = applyClaim(mask, rep.getPhone(), ClaimMask.PHONE);
        mask = applyClaim(mask, rep.getPicture(), ClaimMask.PICTURE);
        mask = applyClaim(mask, rep.getProfile(), ClaimMask.PROFILE);
        mask = applyClaim(mask, rep.getUsername(), ClaimMask.USERNAME);
        mask = applyClaim(mask, rep.getWebsite(), ClaimMask.WEBSITE);

        return mask;
    }

    private static long applyClaim(long mask, boolean condition, long claimMask) {
        if (condition) {
            return mask | claimMask;
        } else {
            return mask & ~claimMask;
        }
    }
}
