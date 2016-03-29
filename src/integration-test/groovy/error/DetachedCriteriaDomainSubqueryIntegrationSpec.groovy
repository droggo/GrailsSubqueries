package error

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import grails.gorm.DetachedCriteria

/**
 * Verifies if subquery returning domains list may be used in parent query
 */
@Integration
@Rollback
class DetachedCriteriaDomainSubqueryIntegrationSpec extends Specification {

	//error: .. (SELECT  FROM CHILD .. - nested query has empty select clause
	//Syntax error in SQL statement "SELECT THIS_.ID AS ID1_1_0_, THIS_.VERSION AS VERSION2_1_0_, THIS_.NAME AS NAME3_1_0_ FROM PARENT THIS_ WHERE THIS_.ID IN (SELECT  FROM CHILD THIS_ INNER JOIN RELATED RELATED_AL1_ ON THIS_.RELATED_ID=RELATED_AL1_.ID WHERE[*] (RELATED_AL1_.NAME IS NOT NULL)) "; expected "(, ., [, ::, *, /, %, +, -, ||, ~, !~, NOT, LIKE, REGEXP, IS, IN, BETWEEN, AND, OR, RIGHT, LEFT, FULL, INNER, JOIN, CROSS, NATURAL, ,, SELECT"
	void 'ensure detached criteria returning domain list may be used in parent query'(){
		DetachedCriteria subquery = new DetachedCriteria(Child).build {
			related {
				isNotNull 'name'
			}
		}

		expect: 'query is executed and returns empty list'
		[] == Parent.createCriteria().list {
			inList 'children', subquery
		} 
	}

	//if id is explicitly set in projections, test will pass
	void 'ensure detached criteria returning domain list with id projection may be used in parent query'(){
		DetachedCriteria subquery = new DetachedCriteria(Child).build {
			projections {
				groupProperty 'id'
			}

			related {
				isNotNull 'name'
			}
		}

		expect: 'query is executed and returns empty list'
		[] == Parent.createCriteria().list {
			inList 'children', subquery
		} 
	}
}