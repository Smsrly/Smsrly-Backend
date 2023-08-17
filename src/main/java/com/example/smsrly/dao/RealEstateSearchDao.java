package com.example.smsrly.dao;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class RealEstateSearchDao {
    private final EntityManager entityManager;

    public Page<RealEstate> findAllByCriteria(
            SearchRequest searchRequest,
            Pageable pageable,
            User user
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);

        Root<RealEstate> root = criteriaQuery.from(RealEstate.class);
        List<Predicate> predicates = new ArrayList<>();

        Predicate ownerPredicate = criteriaBuilder.notEqual(root.get("user"), user);

        // Handle each search criteria if it exists
        if (searchRequest.getTitle() != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchRequest.getTitle() + "%"));
        }
        if (searchRequest.getPrice() != null) {
            predicates.add(criteriaBuilder.equal(root.get("price"), searchRequest.getPrice()));
        }
        if (searchRequest.getArea() != null) {
            predicates.add(criteriaBuilder.equal(root.get("area"), searchRequest.getArea()));
        }
        if (searchRequest.getRoomNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("roomNumber"), searchRequest.getRoomNumber()));
        }
        if (searchRequest.getFloorNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("floorNumber"), searchRequest.getFloorNumber()));
        }
        if (searchRequest.getBathroomNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bathroomNumber"), searchRequest.getBathroomNumber()));
        }
        if (searchRequest.getIsSale() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isSale"), searchRequest.getIsSale()));
        }

        // If no specific criteria, add owner predicate
        if (predicates.isEmpty()) {
            predicates.add(ownerPredicate);
        } else {
            predicates.add(criteriaBuilder.and(ownerPredicate));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<RealEstate> query = entityManager.createQuery(criteriaQuery);

        // Apply paging
        int totalResults = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<RealEstate> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, totalResults);
    }


}
