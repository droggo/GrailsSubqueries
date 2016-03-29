package error

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import grails.gorm.DetachedCriteria

/**
 * Verifies if alias may be defined for already aliased association
 */
@Integration
@Rollback
class DetachedCriteriaAliasIntegrationSpec extends Specification {
	void 'ensure detached criteria with nested alias may be created'(){
		expect: 'query is executed and returns empty list'
		//error - IllegalArgumentException: Argument [c.related] is not an association
		[] == new DetachedCriteria(Parent).build {
			createAlias 'children', 'c'
			createAlias 'c.related', 'r'

			isNotNull 'name'
		}.list()
	}

	//if standard criteria is used, the same query works fine
	void 'ensure criteria with nested alias may be created'(){
		expect: 'query is executed and returns empty list'
		[] == Parent.createCriteria().list {
			createAlias 'children', 'c'
			createAlias 'c.related', 'r'

			isNotNull 'name'
		}
	}
}