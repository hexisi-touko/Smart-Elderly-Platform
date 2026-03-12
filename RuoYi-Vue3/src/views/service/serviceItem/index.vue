<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务商名称" prop="providerName">
        <el-input
          v-model="queryParams.providerName"
          placeholder="请输入服务商名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务项目名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入服务项目名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务类别" prop="itemCategory">
        <el-select v-model="queryParams.itemCategory" placeholder="请选择服务类别" clearable>
          <el-option
            v-for="dict in service_item_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="上架状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择上架状态" clearable>
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
          v-hasPermi="['service:serviceItem:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['service:serviceItem:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['service:serviceItem:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['service:serviceItem:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceItemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务商名称" align="center" prop="providerName" />
      <el-table-column label="服务项目名称" align="center" prop="itemName" />
      <el-table-column label="服务类别" align="center" prop="itemCategory">
        <template #default="scope">
          <dict-tag :options="service_item_category" :value="scope.row.itemCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="标准价格" align="center" prop="standardPrice" />
      <el-table-column label="服务时长" align="center" prop="serviceDuration" />
      <el-table-column label="服务单位" align="center" prop="serviceUnit" />
      <el-table-column label="上架状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['service:serviceItem:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['service:serviceItem:remove']">删除</el-button>
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

    <!-- 添加或修改服务项目管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="serviceItemRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联服务商" prop="providerId">
          <el-select
            v-model="form.providerId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入服务商名称搜索"
            :remote-method="remoteSearchProvider"
            :loading="providerLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in providerOptions"
              :key="item.providerId"
              :label="item.providerName"
              :value="item.providerId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="服务项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入服务项目名称" />
        </el-form-item>
        <el-form-item label="服务类别" prop="itemCategory">
          <el-select v-model="form.itemCategory" placeholder="请选择服务类别">
            <el-option
              v-for="dict in service_item_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务项目描述" prop="itemDescription">
          <el-input v-model="form.itemDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="标准价格" prop="standardPrice">
          <el-input v-model="form.standardPrice" placeholder="请输入标准价格" />
        </el-form-item>
        <el-form-item label="服务时长" prop="serviceDuration">
          <el-input v-model="form.serviceDuration" placeholder="请输入服务时长" />
        </el-form-item>
        <el-form-item label="服务单位" prop="serviceUnit">
          <el-input v-model="form.serviceUnit" placeholder="请输入服务单位" />
        </el-form-item>
        <el-form-item label="上架状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择上架状态">
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

<script setup name="ServiceItem">
import { listServiceItem, getServiceItem, delServiceItem, addServiceItem, updateServiceItem } from "@/api/service/serviceItem"
import { listServiceProvider } from "@/api/service/serviceProvider"

const { proxy } = getCurrentInstance()
const { service_item_category, sys_normal_disable } = proxy.useDict('service_item_category', 'sys_normal_disable')

const serviceItemList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const providerOptions = ref([])
const providerLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    providerId: null,
    providerName: null,
    itemName: null,
    itemCategory: null,
    status: null,
  },
  rules: {
    providerId: [
      { required: true, message: "关联服务商ID不能为空", trigger: "blur" }
    ],
    itemName: [
      { required: true, message: "服务项目名称不能为空", trigger: "blur" }
    ],
    itemCategory: [
      { required: true, message: "服务类别不能为空", trigger: "change" }
    ],
    standardPrice: [
      { required: true, message: "标准价格不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务项目管理列表 */
function getList() {
  loading.value = true
  listServiceItem(queryParams.value).then(response => {
    serviceItemList.value = response.rows
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
    itemId: null,
    providerId: null,
    itemName: null,
    itemCategory: null,
    itemDescription: null,
    standardPrice: null,
    serviceDuration: null,
    serviceUnit: null,
    status: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("serviceItemRef")
}

/** 远程搜索服务商 */
function remoteSearchProvider(query) {
  if (query) {
    providerLoading.value = true
    listServiceProvider({ providerName: query, pageNum: 1, pageSize: 20 }).then(res => {
      providerOptions.value = res.rows
      providerLoading.value = false
    })
  } else {
    providerOptions.value = []
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
  ids.value = selection.map(item => item.itemId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务项目管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _itemId = row.itemId || ids.value
  getServiceItem(_itemId).then(response => {
    form.value = response.data
    if (form.value.providerId) {
      listServiceProvider({ providerId: form.value.providerId, pageNum: 1, pageSize: 10 }).then(res => {
        providerOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改服务项目管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["serviceItemRef"].validate(valid => {
    if (valid) {
      if (form.value.itemId != null) {
        updateServiceItem(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addServiceItem(form.value).then(response => {
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
  const _itemIds = row.itemId || ids.value
  proxy.$modal.confirm('是否确认删除服务项目管理编号为"' + _itemIds + '"的数据项？').then(function() {
    return delServiceItem(_itemIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('service/serviceItem/export', {
    ...queryParams.value
  }, `serviceItem_${new Date().getTime()}.xlsx`)
}

getList()
</script>
