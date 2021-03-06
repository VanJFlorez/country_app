package com.zyos.country.model;

import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static org.hibernate.criterion.Example.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Country entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zyos.model.Country
 * @author MyEclipse Persistence Tools
 */
public class CountryDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(CountryDAO.class);
	// property constants
	public static final String COUNTRYNAME = "countryname";
	public static final String POPULATION = "population";
	public static final String EXTENSION = "extension";
	public static final String GOVTYPE = "govtype";

	/*
	 * https://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html#save(java.lang.Object)
	 * 
	 * https://docs.jboss.org/hibernate/orm/5.1/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-many
	 */
	public void save(Country transientInstance) {
		log.debug("saving Country instance");
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSession();
			tx = session.beginTransaction();
			session.persist(transientInstance);
			getSession().save(transientInstance);
			tx.commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public void delete(Country persistentInstance) {
		log.debug("deleting Country instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Country findById(java.lang.Integer id) {
		log.debug("getting Country instance with id: " + id);
		try {
			Country instance = (Country) getSession().get("com.zyos.model.Country", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Country> findByExample(Country instance) {
		log.debug("finding Country instance by example");
		try {
			List<Country> results = (List<Country>) getSession().createCriteria("com.zyos.model.Country")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Country instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Country as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Country> findByCountryname(Object countryname) {
		return findByProperty(COUNTRYNAME, countryname);
	}

	public List<Country> findByPopulation(Object population) {
		return findByProperty(POPULATION, population);
	}

	public List<Country> findByExtension(Object extension) {
		return findByProperty(EXTENSION, extension);
	}

	public List<Country> findByGovtype(Object govtype) {
		return findByProperty(GOVTYPE, govtype);
	}

	public List findAll() {
		log.debug("finding all Country instances");
		try {
			String queryString = "from Country";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findAllwithWar() {
		log.debug("finding all Country instance with associated Wars");
		try {
			String queryString = "FROM Country c, War w WHERE c.cid = w.country.cid";
			Query queryObject = getSession().createQuery(queryString);
			System.out.println("[CountryDAO.findAllwithWar()]" + queryObject.list());
			return queryObject.list();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public Country merge(Country detachedInstance) {
		log.debug("merging Country instance");
		try {
			Country result = (Country) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Country instance) {
		log.debug("attaching dirty Country instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Country instance) {
		log.debug("attaching clean Country instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}