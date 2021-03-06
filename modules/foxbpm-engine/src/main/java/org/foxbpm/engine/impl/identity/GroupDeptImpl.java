/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author ych
 */
package org.foxbpm.engine.impl.identity;

import java.util.ArrayList;
import java.util.List;

import org.foxbpm.engine.identity.GroupDefinition;
import org.foxbpm.engine.impl.Context;
import org.foxbpm.engine.impl.entity.GroupEntity;
import org.foxbpm.engine.sqlsession.ISqlSession;

public class GroupDeptImpl implements GroupDefinition {

	private String name;
	private String type;
	
	public GroupDeptImpl(String groupType,String groupDefinitionName) {
		this.name = groupDefinitionName;
		this.type = groupType;
	}
	@SuppressWarnings("unchecked")
	public List<GroupEntity> selectGroupByUserId(String userId) {
		ISqlSession sqlsession = Context.getCommandContext().getSqlSession();
		List<GroupEntity> groups = (List<GroupEntity>)sqlsession.selectList("selectDeptByUserId", userId);
		return groups;
	}
	
	 
	public String getType() {
		return type;
	}
	
	 
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@SuppressWarnings("unchecked")
	 
	public List<String> selectUserIdsByGroupId(String groupId) {
		ISqlSession sqlsession = Context.getCommandContext().getSqlSession();
		List<String> userIds = (List<String>)sqlsession.selectList("selectUserIdsByDeptId", groupId);
		return userIds;
	}
	
	 
	public List<GroupEntity> selectChildrenByGroupId(String groupId) {
		List<GroupEntity> groups = new ArrayList<GroupEntity>();
		//获取本身
		GroupEntity group = selectGroupByGroupId(groupId);
		if(group != null){
			groups.add(group);
			//递归子组
			selectSubDept(groupId,groups);
		}
		return groups;
	}
	
	/**
	 * 递归子组
	 * @param groupId
	 * @param groups
	 */
	@SuppressWarnings("unchecked")
	public void selectSubDept(String groupId,List<GroupEntity> groups){
		ISqlSession sqlSession = Context.getCommandContext().getSqlSession();
		List<GroupEntity> tmpGroups = (List<GroupEntity>)sqlSession.selectList("selectDeptBySupId", groupId);
		if(tmpGroups != null){
			groups.addAll(tmpGroups);
			for(GroupEntity tmp : tmpGroups){
				selectSubDept(tmp.getGroupId(),groups);
			}
		}
	}
	
	 
	public GroupEntity selectGroupByGroupId(String groupId) {
		ISqlSession sqlSession = Context.getCommandContext().getSqlSession();
		GroupEntity group = (GroupEntity) sqlSession.selectOne("selectDeptById", groupId);
		return group;
	}
	
	@SuppressWarnings("unchecked")
	 
	public List<GroupEntity> selectAllGroup() {
		ISqlSession sqlSession = Context.getCommandContext().getSqlSession();
		List<GroupEntity> groups = (List<GroupEntity>) sqlSession.selectList("selectAllDept");
		return groups;
	}
	
	@SuppressWarnings("unchecked")
	 
	public List<GroupRelationEntity> selectAllRelation() {
		ISqlSession sqlSession = Context.getCommandContext().getSqlSession();
		List<GroupRelationEntity> groupRelations = (List<GroupRelationEntity>) sqlSession.selectList("selectAllDeptRelation");
		return groupRelations;
	}

}
