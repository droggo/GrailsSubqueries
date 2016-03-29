package error

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import grails.gorm.DetachedCriteria

/**
 * Verifies if multiple projects may be returned in listDistinct
 */
@Integration
@Rollback
class ListDistinctWithPropertyProjectionsIntegrationSpec extends Specification {
	
	//error - only last 'property' is returned
	void 'ensure listDistinct returns all properties'(){
		new Parent(name: 'IntegrationSpec').save(failOnError: true)

		expect: 'query is executed and returns both projected properties'
		Parent.createCriteria().listDistinct {
			projections {
				property 'id'
				property 'name'
			}
		}[0].size() == 2
	}

	//if list() is used, the same query works fine
	void 'ensure list returnes all properties'(){
		new Parent(name: 'IntegrationSpec').save(failOnError: true)

		expect: 'query is executed and returns both projected properties'
		Parent.createCriteria().list {
			projections {
				property 'id'
				property 'name'
			}
		}[0].size() == 2
	}
}