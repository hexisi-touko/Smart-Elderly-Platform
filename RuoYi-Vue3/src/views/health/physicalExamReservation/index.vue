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
      <el-form-item label="体检类型" prop="examType">
        <el-select v-model="queryParams.examType" placeholder="请选择体检类型" clearable>
          <el-option
            v-for="dict in exam_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="体检机构名称" prop="examInstitution">
        <el-input
          v-model="queryParams.examInstitution"
          placeholder="请输入体检机构名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeReservationTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['health:physicalExamReservation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['health:physicalExamReservation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['health:physicalExamReservation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['health:physicalExamReservation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="physicalExamReservationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预约id" align="center" prop="reservationId" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="体检类型" align="center" prop="examType">
        <template #default="scope">
          <dict-tag :options="exam_type" :value="scope.row.examType"/>
        </template>
      </el-table-column>
      <el-table-column label="体检机构名称" align="center" prop="examInstitution" />
      <el-table-column label="预约时间" align="center" prop="reservationTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reservationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="体检地址" align="center" prop="examAddress" />
      <el-table-column label="预约状态" align="center" prop="reservationStatus">
        <template #default="scope">
          <dict-tag :options="exam_reservation_status" :value="scope.row.reservationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="报告状态" align="center" prop="reportStatus">
        <template #default="scope">
          <dict-tag :options="exam_report_status" :value="scope.row.reportStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['health:physicalExamReservation:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['health:physicalExamReservation:remove']">删除</el-button>
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

    <!-- 添加或修改体检预约对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="physicalExamReservationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="体检类型" prop="examType">
          <el-select v-model="form.examType" placeholder="请选择体检类型">
            <el-option
              v-for="dict in exam_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="体检机构名称" prop="examInstitution">
          <el-input v-model="form.examInstitution" placeholder="请输入体检机构名称" />
        </el-form-item>
        <el-form-item label="第三方体检机构ID" prop="thirdPartyOrgId">
          <el-input v-model="form.thirdPartyOrgId" placeholder="请输入第三方体检机构ID" />
        </el-form-item>
        <el-form-item label="预约时间" prop="reservationTime">
          <el-date-picker clearable
            v-model="form.reservationTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预约时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="体检地址" prop="examAddress">
          <el-input v-model="form.examAddress" placeholder="请输入体检地址" />
        </el-form-item>
        <el-form-item label="体检项目清单" prop="examItems">
          <el-input v-model="form.examItems" type="textarea" placeholder="请输入内容" />
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

<script setup name="PhysicalExamReservation">
import { listPhysicalExamReservation, getPhysicalExamReservation, delPhysicalExamReservation, addPhysicalExamReservation, updatePhysicalExamReservation } from "@/api/health/physicalExamReservation"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()
const { exam_type, exam_reservation_status, exam_report_status } = proxy.useDict('exam_type', 'exam_reservation_status', 'exam_report_status')

const physicalExamReservationList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeReservationTime = ref([])
const elderlyOptions = ref([])
const elderlyLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    examType: null,
    examInstitution: null,
    reservationTime: null,
    reservationStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    examType: [
      { required: true, message: "体检类型不能为空", trigger: "change" }
    ],
    examInstitution: [
      { required: true, message: "体检机构名称不能为空", trigger: "blur" }
    ],
    reservationTime: [
      { required: true, message: "预约时间不能为空", trigger: "blur" }
    ],
    examAddress: [
      { required: true, message: "体检地址不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询体检预约列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeReservationTime.value && '' != daterangeReservationTime.value) {
    queryParams.value.params["beginReservationTime"] = daterangeReservationTime.value[0]
    queryParams.value.params["endReservationTime"] = daterangeReservationTime.value[1]
  }
  listPhysicalExamReservation(queryParams.value).then(response => {
    physicalExamReservationList.value = response.rows
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
    reservationId: null,
    elderlyId: null,
    examType: null,
    examInstitution: null,
    thirdPartyOrgId: null,
    reservationTime: null,
    examAddress: null,
    examItems: null,
    reservationStatus: null,
    reportStatus: null,
    reportUrl: null,
    reportInterpret: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("physicalExamReservationRef")
}

/** 远程搜索老人 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      elderlyOptions.value = res.rows
      elderlyLoading.value = false
    })
  } else { elderlyOptions.value = [] }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeReservationTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.reservationId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加体检预约"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _reservationId = row.reservationId || ids.value
  getPhysicalExamReservation(_reservationId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改体检预约"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["physicalExamReservationRef"].validate(valid => {
    if (valid) {
      if (form.value.reservationId != null) {
        updatePhysicalExamReservation(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPhysicalExamReservation(form.value).then(response => {
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
  const _reservationIds = row.reservationId || ids.value
  proxy.$modal.confirm('是否确认删除体检预约编号为"' + _reservationIds + '"的数据项？').then(function() {
    return delPhysicalExamReservation(_reservationIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('health/physicalExamReservation/export', {
    ...queryParams.value
  }, `physicalExamReservation_${new Date().getTime()}.xlsx`)
}

getList()
</script>
