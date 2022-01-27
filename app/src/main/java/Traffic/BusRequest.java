package Traffic;

import androidx.annotation.NonNull;

import Accounts.StandardUser;

public class BusRequest {
    public Station stationOfUser;
    public Route routeUserRequested;
    public StandardUser user;

    public BusRequest(){

    }
    public BusRequest(Station stationOfUser, Route routeUserRequested, StandardUser user){
        this.stationOfUser = stationOfUser;
        this.routeUserRequested = routeUserRequested;
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "The user (" + user.getUsername() + ") is located at " + stationOfUser.Name + " and waiting for bus " + routeUserRequested.getName();
    }
}
