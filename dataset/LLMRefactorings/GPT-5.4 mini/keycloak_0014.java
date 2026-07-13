public class keycloak_0014 {

        public static long getClaimsMask(ClaimRepresentation rep) {
            long claimsMask = ClaimMask.ALL;

            if (rep.getAddress()) {
                claimsMask |= ClaimMask.ADDRESS;
            } else {
                claimsMask &= ~ClaimMask.ADDRESS;
            }
            if (rep.getEmail()) {
                claimsMask |= ClaimMask.EMAIL;
            } else {
                claimsMask &= ~ClaimMask.EMAIL;
            }
            if (rep.getGender()) {
                claimsMask |= ClaimMask.GENDER;
            } else {
                claimsMask &= ~ClaimMask.GENDER;
            }
            if (rep.getLocale()) {
                claimsMask |= ClaimMask.LOCALE;
            } else {
                claimsMask &= ~ClaimMask.LOCALE;
            }
            if (rep.getName()) {
                claimsMask |= ClaimMask.NAME;
            } else {
                claimsMask &= ~ClaimMask.NAME;
            }
            if (rep.getPhone()) {
                claimsMask |= ClaimMask.PHONE;
            } else {
                claimsMask &= ~ClaimMask.PHONE;
            }
            if (rep.getPicture()) {
                claimsMask |= ClaimMask.PICTURE;
            } else {
                claimsMask &= ~ClaimMask.PICTURE;
            }
            if (rep.getProfile()) {
                claimsMask |= ClaimMask.PROFILE;
            } else {
                claimsMask &= ~ClaimMask.PROFILE;
            }
            if (rep.getUsername()) {
                claimsMask |= ClaimMask.USERNAME;
            } else {
                claimsMask &= ~ClaimMask.USERNAME;
            }
            if (rep.getWebsite()) {
                claimsMask |= ClaimMask.WEBSITE;
            } else {
                claimsMask &= ~ClaimMask.WEBSITE;
            }
            return claimsMask;
        }
}
