import {Injectable} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {userCarList} from '../models/userCarList';
import {user} from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  liveToken: boolean;
  alreadyVerified: boolean;
  expiredVerifyToken: boolean;
  token: string;
  submitted: boolean;

  patternForName = '^[a-zA-Z\u00C6\u00D0\u018E\u018F\u0190\u0194\u0132\u014A\u0152\u1E9E\u00DE\u01F7\u021C\u00E6\u00F0\u01DD\u0259\u025B\u0263\u0133\u014B\u0153\u0138\u017F\u00DF\u00FE\u01BF\u021D\u0104\u0181\u00C7\u0110\u018A\u0118\u0126\u012E\u0198\u0141\u00D8\u01A0\u015E\u0218\u0162\u021A\u0166\u0172\u01AFY\u0328\u01B3\u0105\u0253\u00E7\u0111\u0257\u0119\u0127\u012F\u0199\u0142\u00F8\u01A1\u015F\u0219\u0163\u021B\u0167\u0173\u01B0y\u0328\u01B4\u00C1\u00C0\u00C2\u00C4\u01CD\u0102\u0100\u00C3\u00C5\u01FA\u0104\u00C6\u01FC\u01E2\u0181\u0106\u010A\u0108\u010C\u00C7\u010E\u1E0C\u0110\u018A\u00D0\u00C9\u00C8\u0116\u00CA\u00CB\u011A\u0114\u0112\u0118\u1EB8\u018E\u018F\u0190\u0120\u011C\u01E6\u011E\u0122\u0194\u00E1\u00E0\u00E2\u00E4\u01CE\u0103\u0101\u00E3\u00E5\u01FB\u0105\u00E6\u01FD\u01E3\u0253\u0107\u010B\u0109\u010D\u00E7\u010F\u1E0D\u0111\u0257\u00F0\u00E9\u00E8\u0117\u00EA\u00EB\u011B\u0115\u0113\u0119\u1EB9\u01DD\u0259\u025B\u0121\u011D\u01E7\u011F\u0123\u0263\u0124\u1E24\u0126I\u00CD\u00CC\u0130\u00CE\u00CF\u01CF\u012C\u012A\u0128\u012E\u1ECA\u0132\u0134\u0136\u0198\u0139\u013B\u0141\u013D\u013F\u02BCN\u0143N\u0308\u0147\u00D1\u0145\u014A\u00D3\u00D2\u00D4\u00D6\u01D1\u014E\u014C\u00D5\u0150\u1ECC\u00D8\u01FE\u01A0\u0152\u0125\u1E25\u0127\u0131\u00ED\u00ECi\u00EE\u00EF\u01D0\u012D\u012B\u0129\u012F\u1ECB\u0133\u0135\u0137\u0199\u0138\u013A\u013C\u0142\u013E\u0140\u0149\u0144n\u0308\u0148\u00F1\u0146\u014B\u00F3\u00F2\u00F4\u00F6\u01D2\u014F\u014D\u00F5\u0151\u1ECD\u00F8\u01FF\u01A1\u0153\u0154\u0158\u0156\u015A\u015C\u0160\u015E\u0218\u1E62\u1E9E\u0164\u0162\u1E6C\u0166\u00DE\u00DA\u00D9\u00DB\u00DC\u01D3\u016C\u016A\u0168\u0170\u016E\u0172\u1EE4\u01AF\u1E82\u1E80\u0174\u1E84\u01F7\u00DD\u1EF2\u0176\u0178\u0232\u1EF8\u01B3\u0179\u017B\u017D\u1E92\u0155\u0159\u0157\u017F\u015B\u015D\u0161\u015F\u0219\u1E63\u00DF\u0165\u0163\u1E6D\u0167\u00FE\u00FA\u00F9\u00FB\u00FC\u01D4\u016D\u016B\u0169\u0171\u016F\u0173\u1EE5\u01B0\u1E83\u1E81\u0175\u1E85\u01BF\u00FD\u1EF3\u0177\u00FF\u0233\u1EF9\u01B4\u017A\u017C\u017E\u1E93]+$';
  patternForNameWithWhiteSpace = '^[a-zA-Z\s\u00C6\u00D0\u018E\u018F\u0190\u0194\u0132\u014A\u0152\u1E9E\u00DE\u01F7\u021C\u00E6\u00F0\u01DD\u0259\u025B\u0263\u0133\u014B\u0153\u0138\u017F\u00DF\u00FE\u01BF\u021D\u0104\u0181\u00C7\u0110\u018A\u0118\u0126\u012E\u0198\u0141\u00D8\u01A0\u015E\u0218\u0162\u021A\u0166\u0172\u01AFY\u0328\u01B3\u0105\u0253\u00E7\u0111\u0257\u0119\u0127\u012F\u0199\u0142\u00F8\u01A1\u015F\u0219\u0163\u021B\u0167\u0173\u01B0y\u0328\u01B4\u00C1\u00C0\u00C2\u00C4\u01CD\u0102\u0100\u00C3\u00C5\u01FA\u0104\u00C6\u01FC\u01E2\u0181\u0106\u010A\u0108\u010C\u00C7\u010E\u1E0C\u0110\u018A\u00D0\u00C9\u00C8\u0116\u00CA\u00CB\u011A\u0114\u0112\u0118\u1EB8\u018E\u018F\u0190\u0120\u011C\u01E6\u011E\u0122\u0194\u00E1\u00E0\u00E2\u00E4\u01CE\u0103\u0101\u00E3\u00E5\u01FB\u0105\u00E6\u01FD\u01E3\u0253\u0107\u010B\u0109\u010D\u00E7\u010F\u1E0D\u0111\u0257\u00F0\u00E9\u00E8\u0117\u00EA\u00EB\u011B\u0115\u0113\u0119\u1EB9\u01DD\u0259\u025B\u0121\u011D\u01E7\u011F\u0123\u0263\u0124\u1E24\u0126I\u00CD\u00CC\u0130\u00CE\u00CF\u01CF\u012C\u012A\u0128\u012E\u1ECA\u0132\u0134\u0136\u0198\u0139\u013B\u0141\u013D\u013F\u02BCN\u0143N\u0308\u0147\u00D1\u0145\u014A\u00D3\u00D2\u00D4\u00D6\u01D1\u014E\u014C\u00D5\u0150\u1ECC\u00D8\u01FE\u01A0\u0152\u0125\u1E25\u0127\u0131\u00ED\u00ECi\u00EE\u00EF\u01D0\u012D\u012B\u0129\u012F\u1ECB\u0133\u0135\u0137\u0199\u0138\u013A\u013C\u0142\u013E\u0140\u0149\u0144n\u0308\u0148\u00F1\u0146\u014B\u00F3\u00F2\u00F4\u00F6\u01D2\u014F\u014D\u00F5\u0151\u1ECD\u00F8\u01FF\u01A1\u0153\u0154\u0158\u0156\u015A\u015C\u0160\u015E\u0218\u1E62\u1E9E\u0164\u0162\u1E6C\u0166\u00DE\u00DA\u00D9\u00DB\u00DC\u01D3\u016C\u016A\u0168\u0170\u016E\u0172\u1EE4\u01AF\u1E82\u1E80\u0174\u1E84\u01F7\u00DD\u1EF2\u0176\u0178\u0232\u1EF8\u01B3\u0179\u017B\u017D\u1E92\u0155\u0159\u0157\u017F\u015B\u015D\u0161\u015F\u0219\u1E63\u00DF\u0165\u0163\u1E6D\u0167\u00FE\u00FA\u00F9\u00FB\u00FC\u01D4\u016D\u016B\u0169\u0171\u016F\u0173\u1EE5\u01B0\u1E83\u1E81\u0175\u1E85\u01BF\u00FD\u1EF3\u0177\u00FF\u0233\u1EF9\u01B4\u017A\u017C\u017E\u1E93]+$';
  patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]{2,}';
  negatedSet = '^((?![0-9<>{}\\"/|;:~!?@#$%^=&¿§«»ω⊙¤°℃℉€¥£¢¡®©_+]).)*$';

  currentYear: any;
  easterDay;
  easterMonth;
  goodFriday;
  pentecostDay;
  pentecostMonth;
  goodFridayMonth;

  user: user;
  selectedCar: userCarList;
  serviceReservationForm: FormGroup;
  collector: [];
  foreignCountryPlate;
  foreignCountryTax;
  data;
  billingToCompany;

  comment;
  servicesDone;
  selectedCarObj;

  carInspection = [{
    name: 'Minimalista csomag',
    value: 'minimalista csomag'
  }, {
    name: 'Közepes csomag',
    value: 'közepes csomag'
  }, {
    name: 'Maximalista csomag',
    value: 'maximalista csomag'
  }];

  authenticityTest = [{
    name: 'Eredetiség vizsgálat',
    value: 'eredetiség vizsgálat'
  }];

  tyre = [{
    name: 'Gumicsere',
    value: 'gumicsere'
  }, {
    name: 'Kerékcentírozás',
    value: 'kerékcentírozás'
  }, {
    name: 'Defektjavítás',
    value: 'defektjavítás'
  }, {
    name: 'Egyéb',
    value: 'gumi egyéb'
  }];

  brake = [{
    name: 'Fékbetét csere',
    value: 'fékbetét csere'
  }, {
    name: 'Féktárcsa csere',
    value: 'féktárcsa csere'
  }, {
    name: 'Fékfolyadék csere',
    value: 'fékfolyadék csere'
  }, {
    name: 'Fékrendszer csere',
    value: 'fékrendszer csere'
  }, {
    name: 'Egyéb',
    value: 'fék egyéb'
  }
  ];

  undercarriage = [{
    name: 'Futóműellenőrzés',
    value: 'futóműellenőrzés'
  }, {
    name: 'Futóműállítás',
    value: 'futóműállítás'
  }, {
    name: 'Egyéb',
    value: 'futómű egyéb'
  }];

  oil = [{
    name: 'Olajcsere',
    value: 'olajcsere'
  }];

  periodicService = [{
    name: 'Kisszervíz',
    value: 'kiszervíz'
  }, {
    name: 'Nagyszervíz',
    value: 'nagyszervíz'
  }];

  timingBelt = [{
    name: 'Vezérműszíjcsere',
    value: 'vezérműszíjcsere'
  }];

  diagnostic = [{
    name: 'Hibakód kiolvasás',
    value: 'hibakód kiolvasás'
  }];

  technicalExamination = [{
    name: 'Műszaki vizsgáztatás',
    value: 'műszaki vizsgáztatás'
  }, {
    name: 'Műszaki vizsga felkészítés',
    value: 'műszaki vizsga felkészítés'
  }];

  clime = [{
    name: 'Klímatöltés',
    value: 'klmíatöltés'
  }, {
    name: 'Klíma fertőtlenítés',
    value: 'klíma fertőtlenítés'
  }, {
    name: 'Nyomás ellenőrzés',
    value: 'klíma nyomás ellenőrzés'
  }, {
    name: 'Egyéb',
    value: 'klíma egyéb'
  }];

  accumulator = [{
    name: 'Akkumulátor csere',
    value: 'akkumulátor csere'
  }, {
    name: 'Egyéb',
    value: 'akkumulátor egyéb'
  }];

  bodywork = [{
    name: 'Karosszéria javítás',
    value: 'karosszéria javítás'
  }, {
    name: 'Fényezés',
    value: 'fényezés'
  }];

  other = [{
    name: 'Egyéb',
    value: ' egyéb'
  }];

  capture = [{
    name: 'Videó rögzítés',
    value: 'videó rögzítés'
  }, {
    name: 'Kép rögzítés',
    value: 'kép rögzítés'
  }];

  holidayList = [];

  constructor() {
  }

  goodFridayAndEasterAndPentecostCalculator(year) {
    this.currentYear = year;
    let m;
    let n;
    if (1900 <= this.currentYear && 2099 >= this.currentYear) {
      m = 24;
      n = 5;
    }
    if (2100 <= this.currentYear && 2199 >= this.currentYear) {
      m = 24;
      n = 6;
    }
    if (2200 <= this.currentYear && 2299 >= this.currentYear) {
      m = 25;
      n = 0;
    }
    const a = this.currentYear % 19;
    const b = this.currentYear % 4;
    const c = this.currentYear % 7;
    const d = (19 * a + m) % 30;
    const e = (2 * b + 4 * c + 6 * d + n) % 7;
    if ((d + e) < 11) {
      this.easterDay = (d + e + 23);
      this.easterMonth = 3;
    } else {
      this.easterDay = (d + e - 8);
      this.easterMonth = 4;
      if (this.easterDay === 26) {
        this.easterDay = 20;
        this.easterMonth = 4;
      } else if (this.easterDay === 25 && d === 28 && e === 6 && a > 10) {
        this.easterDay = 19;
        this.easterMonth = 4;
      }
    }

    if (this.easterDay > 4) {
      this.goodFriday = this.easterDay - 3;
      this.goodFridayMonth = this.easterMonth;
    } else if (this.easterDay === 3) {
      this.goodFriday = 31;
      this.goodFridayMonth = this.easterMonth - 1;
    } else if (this.easterDay === 2) {
      this.goodFriday = 30;
      this.goodFridayMonth = this.easterMonth - 1;
    } else if (this.easterDay === 1) {
      this.goodFriday = 29;
      this.goodFridayMonth = this.easterMonth - 1;
    }
    let pentecostDayNum = this.easterDay + 49;
    let pentecostMonthNum;
    if (this.easterMonth === 3) {
      pentecostDayNum = pentecostDayNum - 31 - 30;
      pentecostMonthNum = 5;
    } else {
      pentecostDayNum = pentecostDayNum - 30;
      if (pentecostDayNum < 31) {
        pentecostMonthNum = 5;
      } else if (pentecostDayNum > 31) {
        pentecostDayNum = pentecostDayNum - 31;
        pentecostMonthNum = 6;
      }
    }
    this.pentecostDay = pentecostDayNum;
    this.pentecostMonth = pentecostMonthNum;
  }
}
