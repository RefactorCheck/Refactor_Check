public class keycloak_0014 {

        public static long getClaimsMask(ClaimRepresentation rep) {
            long mask = ClaimMask.ALL;
    
            if (rep.getAddress()) {
                mask |= ClaimMask.ADDRESS;
            } else {
                mask &= ~ClaimMask.ADDRESS;
            }
            if (rep.getEmail()) {
                mask |= ClaimMask.EMAIL;
            } else {
                mask &= ~ClaimMask.EMAIL;
            }
            if (rep.getGender()) {
                mask |= ClaimMask.GENDER;
            } else {
                mask &= ~ClaimMask.GENDER;
            }
            if (rep.getLocale()) {
                mask |= ClaimMask.LOCALE;
            } else {
                mask &= ~ClaimMask.LOCALE;
            }
            if (rep.getName()) {
                mask |= ClaimMask.NAME;
            } else {
                mask &= ~ClaimMask.NAME;
            }
            if (rep.getPhone()) {
                mask |= ClaimMask.PHONE;
            } else {
                mask &= ~ClaimMask.PHONE;
            }
            if (rep.getPicture()) {
                mask |= ClaimMask.PICTURE;
            } else {
                mask &= ~ClaimMask.PICTURE;
            }
            if (rep.getProfile()) {
                mask |= ClaimMask.PROFILE;
            } else {
                mask &= ~ClaimMask.PROFILE;
            }
            if (rep.getUsername()) {
                mask |= ClaimMask.USERNAME;
            } else {
                mask &= ~ClaimMask.USERNAME;
            }
            if (rep.getWebsite()) {
                mask |= ClaimMask.WEBSITE;
            } else {
                mask &= ~ClaimMask.WEBSITE;
            }
            return mask;
        }
}
