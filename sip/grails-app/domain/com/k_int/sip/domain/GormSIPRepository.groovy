package com.k_int.sip.domain



class GormSIPRepository extends SIPRepository {


    def gormRepositoryService


    static constraints = {
    }

    /**
     * Return the repository service capable of processing objects from the repository described by this object.
     */
    def repository() {
      gormRepositoryService
    }
}
