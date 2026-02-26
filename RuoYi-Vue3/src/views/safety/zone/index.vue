<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input
          v-model="queryParams.elderlyName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域名称" prop="zoneName">
        <el-input
          v-model="queryParams.zoneName"
          placeholder="请输入区域名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用" prop="isActive">
        <el-select v-model="queryParams.isActive" placeholder="请选择是否启用" clearable>
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['safety:zone:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['safety:zone:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['safety:zone:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['safety:zone:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="zoneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="安全区域id" align="center" prop="zoneId" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="区域名称" align="center" prop="zoneName" />
      <el-table-column label="区域中心经度" align="center" prop="centerLng" />
      <el-table-column label="区域中心纬度" align="center" prop="centerLat" />
      <el-table-column label="区域半径" align="center" prop="radius" />
      <el-table-column label="区域地址描述" align="center" prop="zoneAddress" />
      <el-table-column label="是否启用" align="center" prop="isActive">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.isActive"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['safety:zone:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['safety:zone:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改安全区域/地理围栏对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="zoneRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select
            v-model="form.elderlyId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入老人姓名搜索"
            :remote-method="remoteSearchElderly"
            :loading="elderlyLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in elderlyOptions"
              :key="item.elderlyId"
              :label="item.name + ' (' + (item.phone || '无手机号') + ')'"
              :value="item.elderlyId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="区域名称" prop="zoneName">
          <el-input v-model="form.zoneName" placeholder="请输入区域名称" />
        </el-form-item>
        <el-form-item label="区域中心经度" prop="centerLng">
          <el-input v-model="form.centerLng" placeholder="请输入区域中心经度" />
        </el-form-item>
        <el-form-item label="区域中心纬度" prop="centerLat">
          <el-input v-model="form.centerLat" placeholder="请输入区域中心纬度" />
        </el-form-item>
        <el-form-item label="区域半径" prop="radius">
          <el-input v-model="form.radius" placeholder="请输入区域半径" />
        </el-form-item>
        <el-form-item label="区域地址描述" prop="zoneAddress">
          <el-input v-model="form.zoneAddress" placeholder="请输入区域地址描述" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isActive">
          <el-select v-model="form.isActive" placeholder="请选择是否启用">
            <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Zone">
import { listZone, getZone, delZone, addZone, updateZone } from "@/api/safety/zone"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const zoneList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const elderlyOptions = ref([])
const elderlyLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    zoneName: null,
    isActive: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    zoneName: [
      { required: true, message: "区域名称不能为空", trigger: "blur" }
    ],
    centerLng: [
      { required: true, message: "区域中心经度不能为空", trigger: "blur" }
    ],
    centerLat: [
      { required: true, message: "区域中心纬度不能为空", trigger: "blur" }
    ],
    radius: [
      { required: true, message: "区域半径不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询安全区域/地理围栏列表 */
function getList() {
  loading.value = true
  listZone(queryParams.value).then(response => {
    zoneList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    zoneId: null,
    elderlyId: null,
    zoneName: null,
    centerLng: null,
    centerLat: null,
    radius: null,
    zoneAddress: null,
    isActive: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("zoneRef")
}

/** 远程搜索老人 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      elderlyOptions.value = res.rows
      elderlyLoading.value = false
    })
  } else {
    elderlyOptions.value = []
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.zoneId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加安全区域/地理围栏"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _zoneId = row.zoneId || ids.value
  getZone(_zoneId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => {
        elderlyOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改安全区域/地理围栏"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["zoneRef"].validate(valid => {
    if (valid) {
      if (form.value.zoneId != null) {
        updateZone(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addZone(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _zoneIds = row.zoneId || ids.value
  proxy.$modal.confirm('是否确认删除安全区域/地理围栏编号为"' + _zoneIds + '"的数据项？').then(function() {
    return delZone(_zoneIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('safety/zone/export', {
    ...queryParams.value
  }, `zone_${new Date().getTime()}.xlsx`)
}

getList()
</script>
