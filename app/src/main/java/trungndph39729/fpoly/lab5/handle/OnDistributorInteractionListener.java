package trungndph39729.fpoly.lab5.handle;

import trungndph39729.fpoly.lab5.model.Distributor;

public interface OnDistributorInteractionListener {
    void onDeleteDistributor(String distributorId);
    void onUpdateDistributor(String distributorId, Distributor distributor);
}
