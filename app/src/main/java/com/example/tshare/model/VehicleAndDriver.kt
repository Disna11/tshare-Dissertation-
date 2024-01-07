package com.example.tshare.model
import java.io.Serializable

data class VehicleAndDriver(
    private var vehicleName: String? = null,
    private var model: String? = null,
    private var fuelType: String? = null,
    private var registrationNumber: String? = null,
    private var owner: String? = null,
    private var driverLicense: String? = null,
    private var photo: String? = null,

    ): Serializable {
    // Getter for username
    fun getVehicleName(): String? {
        return vehicleName
    }

    // Setter for username
    fun setUsername(newVehicleName: String?) {
        vehicleName = newVehicleName
    }

    // Getter for email
    fun getModel(): String? {
        return model
    }

    // Setter for email
    fun setModel(newModel: String?) {
        model = newModel
    }

    // Getter for profilePicture
    fun getFuelType(): String? {
        return fuelType
    }

    // Setter for profilePicture
    fun setFuelType(newFuelType: String?) {
        fuelType = newFuelType
    }

    fun getRegistrationNumber(): String? {
        return registrationNumber
    }

    // Setter for username
    fun setRegistrationNumber(newRegistrationNumber: String?) {
        registrationNumber = newRegistrationNumber
    }
    fun getOwner(): String? {
        return owner
    }

    // Setter for username
    fun setOwner(newOwner: String?) {
        owner = newOwner
    }
    fun getDriverLicense(): String? {
        return driverLicense
    }

    // Setter for username
    fun setDriverLicense(newDriverLicense: String?) {
        driverLicense = newDriverLicense
    }

    fun getPhoto(): String? {
        return photo
    }

    fun setPhoto(newPhoto: String?) {
        photo = newPhoto
    }

}
