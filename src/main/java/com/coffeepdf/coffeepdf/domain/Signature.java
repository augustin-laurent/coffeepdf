package com.coffeepdf.coffeepdf.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a digital signature in a PDF document.
 * A signature is defined by its unique identifier, signer name, signed date,
 * certificate data, and coordinates on the page.
 */
public class Signature {
    // Unique identifier for the signature
    private final UUID id;
    // Name of the signer
    private final String signerName;
    // Date and time when the signature was signed
    private final LocalDateTime signedDate;
    // Certificate data used for the signature
    private final byte[] certificate;
    // Coordinates defining the position of the signature on the page
    private Rectangle coordinates;

    /**
     * Creates a signature with the specified parameters.
     *
     * @param idArg          the unique identifier for the signature
     * @param signerNameArg  the name of the signer
     * @param signedDateArg  the date and time when the signature was signed
     * @param certificateArg the certificate data used for the signature
     * @param coordinatesArg the rectangle defining the coordinates of the signature
     */
    public Signature(UUID idArg, String signerNameArg, LocalDateTime signedDateArg, byte[] certificateArg, Rectangle coordinatesArg) {
        id = idArg;
        signerName = signerNameArg;
        signedDate = signedDateArg;
        certificate = certificateArg;
        coordinates = coordinatesArg;
    }

    /**
     * Returns the unique identifier of the signature.
     *
     * @return the UUID of the signature
     */
    public UUID getId() { return id; }
    /**
     * Returns the name of the signer.
     *
     * @return the name of the signer
     */
    public String getSignerName() { return signerName; }
    /**
     * Returns the date and time when the signature was signed.
     *
     * @return the signed date as a LocalDateTime
     */
    public LocalDateTime getSignedDate() { return signedDate; }
    /**
     * Returns the certificate data used for the signature.
     *
     * @return the certificate as a byte array
     */
    public byte[] getCertificate() { return certificate; }
    /**
     * Returns the coordinates defining the position of the signature on the page.
     *
     * @return the Rectangle defining the coordinates
     */
    public Rectangle getCoordinates() { return coordinates; }

    /**
     * Sets the coordinates of the signature.
     *
     * @param coordinatesArg the new Rectangle defining the coordinates
     */
    public void setCoordinates(Rectangle coordinatesArg) { coordinates = coordinatesArg; }

    /**
     * Verifies the digital signature.
     * This method should implement the logic to verify the signature using the certificate.
     *
     * @return true if the signature is valid, false otherwise
     */
    public boolean verify() {
        // TODO : implement signature verification logic
        return false;
    }
}
