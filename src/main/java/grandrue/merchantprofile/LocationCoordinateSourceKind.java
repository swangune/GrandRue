package grandrue.merchantprofile;

/** Provenance class for coordinates accepted into Merchant Location authority. */
public enum LocationCoordinateSourceKind {
    MERCHANT_SELECTED,
    GEOCODER_EVIDENCE,
    IMPORT_EVIDENCE,
    OTHER_ACCEPTED_EVIDENCE
}
